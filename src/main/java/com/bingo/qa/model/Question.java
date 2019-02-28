package com.bingo.qa.model;

import lombok.Data;

import java.util.Date;

/**
 * @author bingo
 */
@Data
public class Question {
    private int id;
    private String title;
    private String content;
    private Date createdDate;
    private int userId;
    private int commentCount;

}
