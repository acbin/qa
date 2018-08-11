package com.bingo.qa.service;

import com.bingo.qa.model.Message;

import java.util.List;

/**
 * Created by bingo on 2018/8/11.
 */

public interface MessageService {
    int addMessage(Message message);

    List<Message> getConversationDetail(String conversationId, int offset, int limit);

    List<Message> getConversationList(int userId, int offset, int limit);

    int getConversationUnreadCount(int userId, String conversationId);

    boolean updateStatus(int id);

    Message getById(int id);
}
