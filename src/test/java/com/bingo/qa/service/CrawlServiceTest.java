package com.bingo.qa.service;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.model.Comment;
import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.Question;
import com.bingo.qa.util.RequestUtil;
import com.bingo.qa.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
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

    }

}
