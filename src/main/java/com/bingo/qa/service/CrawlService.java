package com.bingo.qa.service;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface CrawlService {
    /**
     * 爬虫方法
     * @param type
     * @param pageNum
     */
    void crawl(String type, int pageNum);
}
