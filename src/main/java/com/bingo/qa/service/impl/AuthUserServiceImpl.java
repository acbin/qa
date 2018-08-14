package com.bingo.qa.service.impl;

import com.bingo.qa.dao.AuthUserDAO;
import com.bingo.qa.model.AuthUser;
import com.bingo.qa.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bingo
 * @since 2018/8/14
 */

@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private AuthUserDAO authUserDAO;

    @Override
    public void addAuthUser(int userId) {
        authUserDAO.addAuthUser(userId);
    }

    @Override
    public AuthUser getAuthUser(int userId) {
        return authUserDAO.selectAuthUser(userId);
    }
}
