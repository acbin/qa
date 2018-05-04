package com.bingo.qa.controller;

import com.bingo.qa.model.Comment;
import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.HostHolder;
import com.bingo.qa.service.CommentService;
import com.bingo.qa.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = "/addComment", method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {

        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() == null) {
                return "redirect:/reglogin";
            } else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);

            // 获得总评论数
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            System.out.println("评论数:" + count);

            // 更新问题的总评论数
            questionService.updateCommentCount(comment.getEntityId(), count);


        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
