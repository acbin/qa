package com.bingo.qa.service;

import com.bingo.qa.util.RequestUtil;
import com.bingo.qa.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String url = "https://www.v2ex.com/go/programmer?p=2";

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
                    Element title = doc.selectFirst("div#Main div.box div.header h1");
                    System.out.println(title.text());
                }

                TimeUtil.wait1s();
            }
/*

            List<Document> items = elements.stream()
                    .map(a -> "https://www.v2ex.com" + a.attr("href"))
                    .map(a -> RequestUtil.getDocument(a, header))
                    .filter(a -> a != null)
                    .collect(Collectors.toList());


            for (Document doc : items) {
                Element title = doc.selectFirst("div#Main div.box div.header h1");
                TimeUtil.wait1s();
                System.out.println(title.text());
            }
*/


        }
    }

}
