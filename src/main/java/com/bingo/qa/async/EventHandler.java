package com.bingo.qa.async;


import java.util.List;

/**
 * 每个Handler需要处理事件
 * 碰到关注的event，就去调用doHandler进行处理
 */
public interface EventHandler {
    void doHandler(EventModel model);

    // 注册自己：关注的是哪些EventType
    List<EventType> getSupportEventTypes();

}
