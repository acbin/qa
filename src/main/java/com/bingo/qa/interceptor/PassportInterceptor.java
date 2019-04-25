package com.bingo.qa.interceptor;

import com.bingo.qa.dao.LoginTicketDAO;
import com.bingo.qa.dao.UserDAO;
import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.LoginTicket;
import com.bingo.qa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author bingo
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 在处理controller之前，调用preHandle
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        // 从request中取出ticket
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ticket".equals(cookie.getName())) {
                    // 说明用户的请求携带有ticket
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            // 判断ticket状态: ticket为空/过期/已经失效
            if (loginTicket == null
                    || loginTicket.getExpired().before(new Date())
                    || loginTicket.getStatus() != 0) {
                return true;
            }

            // 此时ticket真实有效
            // 将ticket关联的用户取出来，放入hostHolder上下文中
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }


    /**
     * 在渲染页面之前，回调postHandle
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            // 每次渲染页面之前，先将hostHolder中的user添加至model中
            // 好处就是，在所有的页面，都可以直接访问user变量
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }


    /**
     * 清除处理后留下的信息
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        hostHolder.clear();
    }
}