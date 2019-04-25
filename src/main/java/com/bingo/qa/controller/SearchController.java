package com.bingo.qa.controller;

import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.Question;
import com.bingo.qa.model.ViewObject;
import com.bingo.qa.service.FollowService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.SearchService;
import com.bingo.qa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bingo
 * @since 2018/6/4
 */

@Controller
public class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final SearchService searchService;

    private final FollowService followService;

    private final UserService userService;

    private final QuestionService questionService;

    @Autowired
    public SearchController(SearchService searchService, FollowService followService, UserService userService, QuestionService questionService) {
        this.searchService = searchService;
        this.followService = followService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @GetMapping(value = {"/search"})
    public String search(Model model,
                         @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "30") int count) {

        try {
            // 通过keyword关键词调用搜索服务，获得结果列表
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count, "<span style='background:yellow;'>", "</span>");
            List<ViewObject> vos = new ArrayList<>();

            for (Question question : questionList) {
                // 从数据库获取到每个question的完整数据
                Question q = questionService.getQuestionById(question.getId());

                ViewObject vo = new ViewObject();

                // 替换为高亮后的 content & title
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }

                vo.set("question", q);
                vo.set("user", userService.selectById(q.getUserId()));
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);

        } catch (Exception e) {
            LOGGER.error("搜索评论失败:" + e.getMessage());

        }
        return "result";
    }
}