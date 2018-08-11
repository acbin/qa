package com.bingo.qa.controller;


import com.bingo.qa.service.impl.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    // 用户登录注册
    @GetMapping(value = {"/reglogin"})
    public String reglogin(Model model,
                           @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }


    // 用户注册
    @PostMapping(value = {"/reg"})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next", required = false) String next,
                      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response) {

        try {
            Map<String, String> map = userService.register(username, password);

            if (map.containsKey("ticket")) {
                // map包含ticket，说明用户注册成功，则下发ticket
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    // 若用户勾选"记住我", 则将cookie的有效期设置为5天，关闭浏览器，cookie依然存在
                    // 否则，默认一次cookie在一次会话内有效，关闭浏览器，cookie就没了
                    cookie.setMaxAge(3600 * 24 * 5);
                }

                // 将cookie添加至response，响应给客户端
                response.addCookie(cookie);
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("注册异常: " + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
    }


    // 用户登录
    @PostMapping(value = {"/login"})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                // 若登录成功，同样下发ticket
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }

                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登录异常: " + e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
