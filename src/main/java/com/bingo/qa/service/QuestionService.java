package com.bingo.qa.service;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {

        return questionDAO.selectLatestQuestions(userId, offset, limit);

    }


    public int addQuestion(Question question) {

        // 先过滤html标签
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        // 此处进行敏感词过滤


        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }
}
