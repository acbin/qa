package com.bingo.qa.service.impl;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.model.Question;
import com.bingo.qa.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDAO questionDAO;


    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Question getQuestionById(int id) {
        return questionDAO.selectById(id);
    }

    public int addQuestion(Question question) {
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public void updateCommentCount(int entityId, int count) {
        questionDAO.updateCommentCount(entityId, count);
    }
}
