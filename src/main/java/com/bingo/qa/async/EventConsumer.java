package com.bingo.qa.async;


import com.alibaba.fastjson.JSON;
import com.bingo.qa.util.JedisAdapter;
import com.bingo.qa.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将队列中的event取出,将event与handler关联起来
 *
 * 总体实现思路：
 * 先从上下文获取一系列实现了 Handler 接口的类，每个 Handler 实现类都关注着多个不同类型的事件
 * 我们的目标就是：找出某个类型的事件所对应的一系列 Handler
 * 这样可以根据 EventModel 中的事件类型，交由一系列 Handler 去处理。
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    // 每个eventType对应一系列的eventHandler
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        // 通过applicationContext找到所有实现EventHandler接口的类
        // key为bean的名字，key对应的value为bean的实例(eventHandler)
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            // 此处可以使用 Java 8 Stream 形式
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {

                // 实现EventHandler接口的类
                EventHandler eventHandler = entry.getValue();

                // 找到该Handler所关注的eventType
                List<EventType> eventTypes = eventHandler.getSupportEventTypes();

                if (eventTypes != null) {
                    for (EventType eventType : eventTypes) {
                        if (!config.containsKey(eventType)) {
                            config.put(eventType, new ArrayList<>());
                        }

                        // 为该eventType添加一个eventHandler
                        config.get(eventType).add(eventHandler);
                    }
                }
            }
        }

        // 以上代码执行完后，config包含所有的type以及每个type对应的List<EventHandler>

        new Thread(() -> {
            String key = RedisKeyUtil.getEventQueueKey();
            while (true) {
                // Redis Brpop 命令移出并获取列表的最后一个元素
                // 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
                // 返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
                List<String> events = jedisAdapter.brpop(0, key);
                for (String message : events) {
                    if (message.equals(key)) {
                        continue;
                    }

                    // 将message json字符串转换为eventModel对象
                    EventModel eventModel = JSON.parseObject(message, EventModel.class);
                    if (!config.containsKey(eventModel.getType())) {
                        // 该事件包含系统不能识别的eventType，直接跳过
                        logger.error("不能识别的eventType");
                        continue;
                    }

                    EventType type = eventModel.getType();

                    // 交给该eventType对应的一系列handler处理
                    for (EventHandler handler : config.get(type)) {
                        handler.doHandler(eventModel);
                    }

                }
            }
        }).start();
    }

}
