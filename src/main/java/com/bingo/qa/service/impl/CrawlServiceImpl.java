package com.bingo.qa.service.impl;

import com.bingo.qa.service.CrawlService;
import com.bingo.qa.util.RequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/** 爬虫service
 * @author bingo
 * @since 2018/8/11
 */

@Service
public class CrawlServiceImpl implements CrawlService {

    @Override
    public void crawl() {
        String url = "https://www.v2ex.com/go/programmer?p=1";
        Document document = RequestUtil.getDocument(url);
        if (document != null) {
            Elements elements = document.select("div#TopicsNode table td span.item_title a");



        }


    }
}
