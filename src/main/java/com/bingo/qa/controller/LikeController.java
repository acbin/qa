package com.bingo.qa.controller;

import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventProducer;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.Comment;
import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.User;
import com.bingo.qa.service.impl.CommentServiceImpl;
import com.bingo.qa.service.impl.LikeService;
import com.bingo.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentServiceImpl commentServiceImpl;

    @PostMapping(value = {"/like"})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (user == null) {
            // 用户未登录，直接返回
            return QaUtil.getJSONString(999);
        }

        Comment comment = commentServiceImpl.getCommentById(commentId);

        // 用户点了个赞，那么就发送一个event出去，通知被点赞的评论的owner
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(user.getId())
                .setEntityId(commentId)
                .setEntityOwnerId(comment.getUserId())
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setExt("questionId", comment.getEntityId() + "")
        );

        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_COMMENT, commentId);
        return QaUtil.getJSONString(0, likeCount + "");
    }


    @PostMapping(value = {"/dislike"})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (user == null) {
            // 用户未登录，直接返回
            return QaUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(user.getId(), EntityType.ENTITY_COMMENT, commentId);
        return QaUtil.getJSONString(0, likeCount + "");
    }
}
