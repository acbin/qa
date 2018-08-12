package com.bingo.qa.controller;

import com.bingo.qa.service.CrawlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 爬虫controller
 * @author bingo
 * @since 2018/8/12
 */

@RestController
@RequestMapping("/crawl")
public class CrawlController {

    private Logger LOGGER = LoggerFactory.getLogger(CrawlController.class);
    @Autowired
    private CrawlService crawlService;

    @GetMapping
    public String crawl(@RequestParam(value = "type") String type,
                        @RequestParam(value = "pageNum") int pageNum) {
        LOGGER.info("start crawling");
        crawlService.crawl(type, pageNum);
        LOGGER.info("finish crawling");
        return "ok";
    }
}
