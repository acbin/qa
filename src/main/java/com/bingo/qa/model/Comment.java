package com.bingo.qa.model;

import lombok.Data;

import java.util.Date;

/**
 * @author bingo
 */
@Data
public class Comment {
    private int id;
    private int userId;
    private int entityId;
    private int entityType;
    private String content;
    private Date createdDate;
    private int status;
}
