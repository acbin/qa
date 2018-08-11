package com.bingo.qa.service.impl;

import com.bingo.qa.dao.MessageDAO;
import com.bingo.qa.model.Message;
import com.bingo.qa.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bingo
 * @since 2018/8/11
 */

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    public boolean updateStatus(int id) {
        return messageDAO.updateStatus(id, 1) > 0;
    }

    public Message getById(int id) {
        return messageDAO.getById(id);
    }

}
