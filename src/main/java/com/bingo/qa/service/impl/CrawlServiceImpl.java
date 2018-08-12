package com.bingo.qa.service.impl;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.model.Question;
import com.bingo.qa.service.CrawlService;
import com.bingo.qa.util.RequestUtil;
import com.bingo.qa.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** 爬虫service
 * @author bingo
 * @since 2018/8/11
 */

@Service
public class CrawlServiceImpl implements CrawlService {

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public void crawl() {
        String url = "https://www.v2ex.com/go/programmer?p=1";
        Map<String, String> header = new HashMap<>();
        header.put("Host", "www.v2ex.com");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Accept-Encoding", "gzip, deflate, br");

        Document document = RequestUtil.getDocument(url, header);
        if (document != null) {
            Elements elements = document.select("div#TopicsNode table td span.item_title a");

            for (Element ele : elements) {
                String href = "https://www.v2ex.com" + ele.attr("href");
                Document doc = RequestUtil.getDocument(href, header);
                if (doc != null) {
                    String title = doc.selectFirst("div#Main div.box div.header h1").text();
                    String content = doc.selectFirst("#Main > div:nth-child(2) > div.cell > div > div > p").text();

                    Question question = new Question();
                    question.setUserId(new Random().nextInt(16));
                    question.setContent(content);
                    question.setCreatedDate(new Date());
                    question.setTitle(title);
                    question.setCommentCount(0);

                }

                // 休眠1s
                TimeUtil.wait1s();
            }



        }


    }
}
