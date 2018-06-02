package com.bingo.qa.async.handler;


import com.alibaba.fastjson.JSONObject;
import com.bingo.qa.async.EventHandler;
import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.*;
import com.bingo.qa.service.FeedService;
import com.bingo.qa.service.FollowService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.UserService;
import com.bingo.qa.util.JedisAdapter;
import com.bingo.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler{

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

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

        // data包括:userId, userHead, userName, questionId, questionTitle
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }

        // 将触发的事件存入数据库中
        feedService.addFeed(feed);


        // 将事件推给关注的粉丝(将事件的id存入每个关注该事件的粉丝的timeline中)
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        followers.add(0);
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimeLineKey(follower);
            jedisAdapter.lpush(timelineKey, feed.getId() + "");
        }

        // to do....
        // 当用户取消关注的时候，需要将timeline中的事件id移出



    }

    @Override
    public List<EventType> getSupportEventTypes() {
        // 该FeedHandler监听COMMENT、FOLLOW事件
        return Arrays.asList(new EventType[] {EventType.COMMENT, EventType.FOLLOW});
    }
}
