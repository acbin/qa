package com.bingo.qa.service;

import com.bingo.qa.model.User;

import java.util.Map;

/**
 * Created by bingo on 2018/8/11.
 */

public interface UserService {
    User selectById(int id);

    User selectByName(String name);

    Map<String, String> register(String username, String password);

    Map<String, String> login(String username, String password);

    void logout(String ticket);
}
