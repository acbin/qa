package com.bingo.qa.async.handler;


import com.alibaba.fastjson.JSONObject;
import com.bingo.qa.async.EventHandler;
import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.*;
import com.bingo.qa.service.FeedService;
import com.bingo.qa.service.MessageService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.selectById(model.getActorId());
        if (actor == null) {
            return null;
        }

        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.getQuestionById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", question.getId() + "");
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }

        return null;
    }

    @Override
    public void doHandler(EventModel model) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }

        feedService.addFeed(feed);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[] {EventType.COMMENT, EventType.FOLLOW});
    }
}
