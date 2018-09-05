package com.bingo.qa.controller;

import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.User;
import com.bingo.qa.service.AuthUserService;
import com.bingo.qa.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 爬虫controller
 *
 * @author bingo
 * @since 2018/8/12
 */

@Controller
@RequestMapping("/crawl")
public class CrawlController {

    @Autowired
    private CrawlService crawlService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping
    public String crawl(@RequestParam(value = "type") String type,
                        @RequestParam(value = "pageNum") int pageNum) {

        User user = hostHolder.getUser();

        if (user == null || authUserService.getAuthUser(user.getId()) == null) {
            System.out.println("未登录/未授权爬虫");
            return "redirect:/index";
        }

        crawlService.crawl(type, pageNum);

        return "redirect:/index";
    }
}
