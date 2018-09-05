package com.bingo.qa.service;

import com.bingo.qa.model.Question;

import java.util.List;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface QuestionService {
    /**
     * 查找最近问题列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Question> selectLatestQuestions(int userId, int offset, int limit);

    /**
     * 根据id获取问题
     * @param id
     * @return
     */
    Question getQuestionById(int id);

    /**
     * 添加问题
     * @param question
     * @return
     */
    int addQuestion(Question question);

    /**
     * 更新问题评论数
     * @param entityId
     * @param count
     */
    void updateCommentCount(int entityId, int count);

    /**
     * 获取问题数
     * @return
     */
    long getQuestionCount();
}
