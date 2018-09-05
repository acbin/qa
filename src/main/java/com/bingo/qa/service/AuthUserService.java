package com.bingo.qa.service;

import com.bingo.qa.model.AuthUser;

/**
 * @author bingo
 * @since 2018/8/14
 */

public interface AuthUserService {
    /**
     * 添加授权用户
     * @param userId
     */
    void addAuthUser(int userId);

    /**
     * 根据userId获取授权用户
     * @param userId
     * @return
     */
    AuthUser getAuthUser(int userId);
}
