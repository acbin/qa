package com.bingo.qa.controller;


import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.Question;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;


    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {

        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                return QaUtil.getJSONString(999);
            }

            question.setUserId(hostHolder.getUser().getId());

            int res = questionService.addQuestion(question);
            if (res > 0) {
                return QaUtil.getJSONString(0);
            }

        } catch (Exception e) {
            logger.error("增加问题失败" + e.getMessage());
        }

        return QaUtil.getJSONString(999);
    }



}
