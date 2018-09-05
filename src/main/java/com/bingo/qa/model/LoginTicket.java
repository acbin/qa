package com.bingo.qa.model;

import lombok.Data;

import java.util.Date;

/**
 * @author bingo
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;
    private String ticket;
    private int status;
}
