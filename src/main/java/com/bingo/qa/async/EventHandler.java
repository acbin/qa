package com.bingo.qa.async;


import java.util.List;

/**
 * 每个Handler需要处理事件
 * 碰到关注的event，就去调用doHandler进行处理
 *
 * @author bingo
 */
public interface EventHandler {
    /**
     * 处理model事件
     * @param model
     */
    void doHandler(EventModel model);

    /**
     * 注册自己：关注的是哪些EventType
     * @return list
     */
    List<EventType> getSupportEventTypes();

}
