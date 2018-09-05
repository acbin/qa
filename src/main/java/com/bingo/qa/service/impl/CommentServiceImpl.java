package com.bingo.qa.service.impl;

import com.bingo.qa.dao.CommentDAO;
import com.bingo.qa.model.Comment;
import com.bingo.qa.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bingo
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Override
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    @Override
    public int addComment(Comment comment) {
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    @Override
    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    @Override
    public boolean deleteComment(int commentId) {
        return commentDAO.updateStatus(commentId, 1) > 0;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }

    @Override
    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }

}
