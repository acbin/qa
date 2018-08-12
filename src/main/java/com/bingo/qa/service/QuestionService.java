package com.bingo.qa.service;

import com.bingo.qa.model.Question;

import java.util.List;

/**
 * Created by bingo on 2018/8/11.
 */

public interface QuestionService {
    List<Question> selectLatestQuestions(int userId, int offset, int limit);

    Question getQuestionById(int id);

    int addQuestion(Question question);

    void updateCommentCount(int entityId, int count);

    long getQuestionCount();
}
