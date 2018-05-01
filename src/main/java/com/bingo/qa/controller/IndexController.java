package com.bingo.qa.controller;

import com.bingo.qa.model.Question;
import com.bingo.qa.model.ViewObject;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        List<ViewObject> vos = getQuestions(0, 0, 10);
        model.addAttribute("vos", vos);
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.selectLatestQuestions(userId, offset, limit);

        List<ViewObject> vos = new ArrayList<>();

        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.selectById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        List<ViewObject> vos = getQuestions(userId, 0, 10);
        model.addAttribute("vos", vos);
        return "index";
    }

}
