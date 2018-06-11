package com.bingo.qa.service;

import com.bingo.qa.dao.LoginTicketDAO;
import com.bingo.qa.dao.UserDAO;
import com.bingo.qa.model.LoginTicket;
import com.bingo.qa.model.User;
import com.bingo.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User selectById(int id) {
        return userDAO.selectById(id);
    }

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }


    // 注册
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        // 判断用户是否已存在
        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        // 用户不存在，可以创建用户
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl("/images/avatar/" + username + ".png");

        // 利用md5和盐加密用户密码
        user.setPassword(QaUtil.MD5(password + user.getSalt()));
        try {
            QaUtil.createIdenticon(username, 200);
        } catch (IOException e) {
            map.put("msg", "头像生成失败，请重试");
            return map;
        }
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;

    }

    // 登录
    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!QaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }

        // 下发ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;

    }


    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime() + 3600 * 24 * 100 );
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    /**
     * 用户退出，将ticket的status设置为1
     * @param ticket
     */
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

}
