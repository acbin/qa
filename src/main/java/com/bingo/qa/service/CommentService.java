package com.bingo.qa.service;

import com.bingo.qa.model.Comment;

import java.util.List;

/**
 * Created by bingo on 2018/8/11.
 */

public interface CommentService {
    List<Comment> getCommentsByEntity(int entityId, int entityType);

    int addComment(Comment comment);

    int getCommentCount(int entityId, int entityType);

    boolean deleteComment(int commentId);

    Comment getCommentById(int id);

    int getUserCommentCount(int userId);
}
