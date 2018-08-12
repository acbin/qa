package com.bingo.qa.controller;

import com.bingo.qa.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 爬虫controller
 * @author bingo
 * @since 2018/8/12
 */

@Controller
@RequestMapping("/crawl")
public class CrawlController {

    @Autowired
    private CrawlService crawlService;

    @GetMapping
    public String crawl(@RequestParam(value = "type") String type,
                        @RequestParam(value = "pageNum") int pageNum) {
        crawlService.crawl(type, pageNum);
        return "200";
    }
}
