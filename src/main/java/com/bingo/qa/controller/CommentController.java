package com.bingo.qa.controller;

import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventProducer;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.*;
import com.bingo.qa.service.CommentService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.SensitiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * @author bingo
 */
@Controller
public class CommentController {

    private static Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    private final HostHolder hostHolder;

    private final CommentService commentService;

    private final QuestionService questionService;

    private final SensitiveService sensitiveService;

    private final EventProducer eventProducer;

    @Autowired
    public CommentController(HostHolder hostHolder, CommentService commentService, QuestionService questionService, SensitiveService sensitiveService, EventProducer eventProducer) {
        this.hostHolder = hostHolder;
        this.commentService = commentService;
        this.questionService = questionService;
        this.sensitiveService = sensitiveService;
        this.eventProducer = eventProducer;
    }

    /**
     *
     * @param questionId 评论所对应的问题ID
     * @param content 评论的内容
     * @return question页面
     */
    @PostMapping(value = {"/addComment"})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {

        try {
            // 从hostHolder中取出用户
            User user = hostHolder.getUser();
            if (user == null) {
                // 未登录，直接重定向到登录页面
                return "redirect:/reglogin";
            }

            Comment comment = new Comment();
            comment.setUserId(user.getId());

            // 过滤评论内容中的html标签和敏感词
            content = sensitiveService.filter(HtmlUtils.htmlEscape(content));
            comment.setContent(content);

            // 设置评论时间
            comment.setCreatedDate(new Date());

            // 设置评论对应的实体类型和ID
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);

            // 将评论添加至数据库
            commentService.addComment(comment);

            // 查出当前评论对应的问题的总评论数
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());

            // 因为增加了一条评论，所以需要更新问题的总评论数
            questionService.updateCommentCount(comment.getEntityId(), count);

            Question question = questionService.getQuestionById(questionId);

            // 这是一个评论事件
            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(comment.getUserId())
                    .setEntityOwnerId(question.getUserId())
                    .setEntityId(question.getId()));

        } catch (Exception e) {
            LOGGER.error("增加评论失败" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }

}