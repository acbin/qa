package com.bingo.qa.model;

import lombok.Data;

/**
 * @author bingo
 */
@Data
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;
}
