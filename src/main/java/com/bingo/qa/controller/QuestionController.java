package com.bingo.qa.controller;


import com.bingo.qa.model.*;
import com.bingo.qa.service.CommentService;
import com.bingo.qa.service.LikeService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.UserService;
import com.bingo.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {

        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                return QaUtil.getJSONString(999);
            }

            question.setUserId(hostHolder.getUser().getId());

            int res = questionService.addQuestion(question);
            if (res > 0) {
                return QaUtil.getJSONString(0);
            }

        } catch (Exception e) {
            logger.error("增加问题失败" + e.getMessage());
        }

        return QaUtil.getJSONString(999);
    }


    @RequestMapping(value = "/question/{qid}", method = RequestMethod.GET)
    public String questionDetail(Model model,
                                 @PathVariable("qid") int qid) {
        Question question = questionService.getQuestionById(qid);
        model.addAttribute("question", question);
        // model.addAttribute("user", userService.selectById(question.getUserId()));
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.selectById(comment.getUserId()));
            vos.add(vo);
        }

        model.addAttribute("vos", vos);

        return "detail";
    }

}
