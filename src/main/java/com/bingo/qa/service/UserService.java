package com.bingo.qa.service;

import com.bingo.qa.dao.UserDAO;
import com.bingo.qa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User selectById(int id) {
        return userDAO.selectById(id);
    }
}
