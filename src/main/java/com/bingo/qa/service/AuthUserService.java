package com.bingo.qa.service;

import com.bingo.qa.model.AuthUser;

/**
 * @author bingo
 * @since 2018/8/14
 */

public interface AuthUserService {
    void addAuthUser(int userId);

    AuthUser getAuthUser(int userId);
}
