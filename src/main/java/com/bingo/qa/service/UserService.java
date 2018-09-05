package com.bingo.qa.service;

import com.bingo.qa.model.User;

import java.util.Map;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface UserService {
    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    User selectById(int id);

    /**
     * 根据名字查找用户
     * @param name
     * @return
     */
    User selectByName(String name);

    /**
     * 注册用户
     * @param username
     * @param password
     * @return
     */
    Map<String, String> register(String username, String password);

    /**
     * 登录用户
     * @param username
     * @param password
     * @return
     */
    Map<String, String> login(String username, String password);

    /**
     * 注销用户
     * @param ticket
     */
    void logout(String ticket);
}
