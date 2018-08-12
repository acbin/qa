package com.bingo.qa.service.impl;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.model.Comment;
import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.Question;
import com.bingo.qa.service.CommentService;
import com.bingo.qa.service.CrawlService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.SensitiveService;
import com.bingo.qa.util.QaUtil;
import com.bingo.qa.util.RequestUtil;
import com.bingo.qa.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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

    @Autowired
    private SensitiveService sensitiveService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Override
    public void crawl(String type, int pageNum) {
        // 获得字符串
        String url = RequestUtil.getUrl(type, pageNum);

        // 封装请求头
        Map<String, String> header = new HashMap<>();
        header.put("Host", "www.v2ex.com");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Accept-Encoding", "gzip, deflate, br");

        // 获取文档document对象
        Document document = RequestUtil.getDocument(url, header);
        if (document != null) {
            // 获取页面上所有question的a标签
            Elements elements = document.select("div#TopicsNode table td span.item_title a");

            for (Element ele : elements) {

                // 获取每个question详情页的url
                String href = "https://www.v2ex.com" + ele.attr("href");
                Document doc = RequestUtil.getDocument(href, header);
                if (doc != null) {
                    // 获取标题
                    String title = doc.selectFirst("div#Main div.box div.header h1").text();

                    // 获取question的内容
                    Element e = doc.selectFirst("div#Main div.box div.cell div.topic_content div.markdown_body p");
                    e = e == null ? doc.selectFirst("div#Main div.box div.cell div.topic_content") : e;
                    String content = e == null ? "内容如题" : e.ownText();

                    // 创建question对象并设置相应属性
                    Question question = new Question();
                    question.setUserId(new Random().nextInt(16) + 1);


                    question.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(QaUtil.dealString(content))));
                    question.setCreatedDate(new Date());
                    question.setTitle(title);
                    question.setCommentCount(0);

                    questionService.addQuestion(question);
                    long count = questionDAO.getQuestionCount();

                    Elements comments = doc.select("table > tbody > tr div.reply_content");

                    comments.stream().map(a -> a.text()).filter(txt -> !txt.contains("@")).forEach(txt -> {
                        Comment comment = new Comment();
                        comment.setUserId(new Random().nextInt(16) + 1);
                        comment.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(QaUtil.dealString(txt))));
                        comment.setCreatedDate(new Date());

                        // 设置评论所对应的实体的id
                        comment.setEntityId((int) count);
                        comment.setEntityType(EntityType.ENTITY_QUESTION);
                        commentService.addComment(comment);
                        int commentCount = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
                        questionService.updateCommentCount(comment.getEntityId(), commentCount);
                    });

                }

                // 休眠2s,防止ip被禁
                TimeUtil.wait2s();
            }

        }

    }
}
