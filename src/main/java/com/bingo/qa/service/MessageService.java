package com.bingo.qa.service;

import com.bingo.qa.model.Message;

import java.util.List;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface MessageService {
    /**
     * 添加私信
     * @param message
     * @return
     */
    int addMessage(Message message);

    /**
     * 获取私信详情
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationDetail(String conversationId, int offset, int limit);

    /**
     * 获取私信列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationList(int userId, int offset, int limit);

    /**
     * 获取未读数
     * @param userId
     * @param conversationId
     * @return
     */
    int getConversationUnreadCount(int userId, String conversationId);

    /**
     * 更新状态
     * @param id
     * @return
     */
    boolean updateStatus(int id);

    /**
     * 根据id获取私信
     * @param id
     * @return
     */
    Message getById(int id);
}
