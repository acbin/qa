package com.bingo.qa.model;

import lombok.Data;

import java.util.Date;

/**
 * @author bingo
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;

    /**
     * 保证无论在 from 方还是 to 方，conversation_id 都是一致的
     *
     * @return str
     */
    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d", fromId, toId);
        }
        return String.format("%d_%d", toId, fromId);
    }

}
