package com.bingo.qa.task;

import com.bingo.qa.service.CrawlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 爬虫定时任务
 *
 * @author bingo
 * @since 2018/8/12
 */

@Component
public class CrawlTask {

    private static Logger LOGGER = LoggerFactory.getLogger(CrawlTask.class);

    private final CrawlService crawlService;

    @Autowired
    public CrawlTask(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void crawl() {
        LOGGER.info("start crawling");

        crawlService.crawl("programmer", 1);

        LOGGER.info("finish crawling");
    }
}
