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

@Service
/**
 * 将队列中的event取出,将event与handler关联起来
 */
public class EventConsumer implements InitializingBean, ApplicationContextAware{

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    // 每个EventType，对应一系列的EventHandler
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        //找到所有实现EventHandler接口的类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                // 找出每个Handler所关注的EventType
                // entry.getValue()就是一个Handler
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                if (eventTypes != null) {
                    for (EventType type : eventTypes) {
                        // 如果config不包含该type，添加至config中
                        if (!config.containsKey(type)) {
                            config.put(type, new ArrayList<>());
                        }
                        config.get(type).add(entry.getValue());
                    }
                }

            }
        }

        // 以上代码执行完后，config包含所有的type以及每个type对应的List<EventHandler>

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    // 如果没有取到，就一直卡着
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的type");
                            continue;
                        }
                        EventType type = eventModel.getType();
                        for (EventHandler handler : config.get(type)) {
                            handler.doHandler(eventModel);
                        }

                    }
                }
            }
        });

        thread.start();




    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
