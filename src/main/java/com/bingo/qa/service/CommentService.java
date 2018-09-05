package com.bingo.qa.service;

import com.bingo.qa.model.Comment;

import java.util.List;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface CommentService {
    /**
     * 通过实体获取评论列表
     * @param entityId
     * @param entityType
     * @return
     */
    List<Comment> getCommentsByEntity(int entityId, int entityType);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    int addComment(Comment comment);

    /**
     * 获取评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    int getCommentCount(int entityId, int entityType);

    /**
     * 根据id获取评论
     * @param id
     * @return
     */
    Comment getCommentById(int id);

    /**
     * 根据用户id获取评论
     * @param userId
     * @return
     */
    int getUserCommentCount(int userId);
}
