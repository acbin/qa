package com.bingo.qa.async.handler;


import com.alibaba.fastjson.JSONObject;
import com.bingo.qa.async.EventHandler;
import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.*;
import com.bingo.qa.service.impl.FeedServiceImpl;
import com.bingo.qa.service.impl.FollowServiceImpl;
import com.bingo.qa.service.impl.QuestionServiceImpl;
import com.bingo.qa.service.impl.UserServiceImpl;
import com.bingo.qa.util.JedisAdapter;
import com.bingo.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler{

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    QuestionServiceImpl questionServiceImpl;

    @Autowired
    FeedServiceImpl feedServiceImpl;

    @Autowired
    FollowServiceImpl followService;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 构建新鲜事数据
     * @param model
     * @return
     */
    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userServiceImpl.selectById(model.getActorId());
        if (actor == null) {
            return null;
        }

        map.put("userId", actor.getId() + "");
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        // 当前是一个评论事件或者是一个用户关注了一个问题(排除"用户关注的是人"的情况)
        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionServiceImpl.getQuestionById(model.getEntityId());
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

        // 新鲜事是谁发的
        feed.setUserId(model.getActorId());

        // 新鲜事的类型
        feed.setType(model.getType().getValue());

        // data包括:userId, userHead, userName, questionId, questionTitle
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }

        // 将新鲜事数据(json格式)存入数据库中
        feedServiceImpl.addFeed(feed);

        // 找出触发该事件的用户的粉丝
        List<Integer> followers = followService.getFollowers(
                EntityType.ENTITY_USER,
                model.getActorId(),
                Integer.MAX_VALUE);

        followers.add(0);
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimeLineKey(follower);
            // 推模式
            // 将新鲜事id放入每个粉丝的timeline(redis list)中
            jedisAdapter.lpush(timelineKey, feed.getId() + "");
        }

        // to do....
        // 当用户取消关注的时候，需要将timeline中的事件id移出

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        // 该FeedHandler监听COMMENT、FOLLOW事件
        // 当有评论或者关注事件发生时，此handler会被调用
        return Arrays.asList(new EventType[] {EventType.COMMENT, EventType.FOLLOW});
    }
}