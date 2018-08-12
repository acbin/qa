package com.bingo.qa.service;

import com.bingo.qa.util.RequestUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bingo
 * @since 2018/8/12
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CrawlServiceTest {

    @Test
    public void testCrawl() {
        String url = "https://www.v2ex.com/go/programmer?p=1";
        Document document = RequestUtil.getDocument(url);
        if (document != null) {
            Elements elements = document.select("div#TopicsNode table td span.item_title a");
            List<Document> items = elements.stream()
                    .map(a -> "https://www.v2ex.com" + a.attr("href"))
                    .map(a -> RequestUtil.getDocument(a))
                    .filter(a -> a != null)
                    .collect(Collectors.toList());


            for (Document doc : items) {
                Element title = doc.selectFirst("div#Main div.box div.header h1");
                System.out.println(title.text());
            }


        }
    }

}
