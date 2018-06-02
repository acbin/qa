package com.bingo.qa.controller;

import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventProducer;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.*;
import com.bingo.qa.service.*;
import com.bingo.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bingo on 2018/5/31.
 */

@Controller
public class FollowController {
    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @PostMapping(value = {"/followUser"})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return QaUtil.getJSONString(999);
        }

        // 当前登录的用户，关注了另一个人
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        // 当前登陆用户关注了用户A，触发一个event
        // 需要向A发送消息, entityId与entityOwnerId均为用户A的id
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
        );

        Map<String, Object> info = new HashMap<>();
        // 粉丝数
        info.put("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));

        // 关注数
        info.put("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));

        // 回答数
        info.put("commentCount", commentService.getUserCommentCount(userId));

        // 赞同数
        info.put("likeCount", likeService.getLikeCount(EntityType.ENTITY_USER, userId));


        // 若成功，返回0 和 当前登录用户所关注的用户数量
        return QaUtil.getJSONString(ret ? 0 : 1, info);

    }

    @PostMapping(value = {"/unfollowUser"})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return QaUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
        );

        Map<String, Object> info = new HashMap<>();
        // 粉丝数
        info.put("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));

        // 关注数
        info.put("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));

        // 回答数
        info.put("commentCount", commentService.getUserCommentCount(userId));

        // 赞同数
        info.put("likeCount", likeService.getLikeCount(EntityType.ENTITY_USER, userId));


        // 若成功，返回0 和 当前登录用户所关注的用户数量
        return QaUtil.getJSONString(ret ? 0 : 1, info);

    }

    /**
     * 关注问题
     * @param questionId
     * @return
     */
    @PostMapping(value = {"/followQuestion"})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return QaUtil.getJSONString(999);
        }

        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return QaUtil.getJSONString(1, "问题不存在");
        }

        // 当前登录的用户，关注了某一个问题
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        // 触发事件，向该问题所属的用户发送一条Message
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityOwnerId(question.getUserId())
                .setEntityType(EntityType.ENTITY_QUESTION)
        );


        Map<String, Object> info = new HashMap<>();

        // 将当前用户的信息写入map，并返回到页面
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());

        // 当前问题的关注者数量
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        // 若成功，返回0 和 当前问题的关注者信息
        return QaUtil.getJSONString(ret ? 0 : 1, info);

    }

    /**
     * 取消关注问题
     * @param questionId
     * @return
     */
    @PostMapping(value = {"/unfollowQuestion"})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return QaUtil.getJSONString(999);
        }

        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return QaUtil.getJSONString(1, "问题不存在");
        }

        // 当前登录的用户，关注了某一个问题
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityOwnerId(question.getUserId())
                .setEntityType(EntityType.ENTITY_QUESTION)
        );

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());

        // 当前问题的关注者数量
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        // 若成功，返回0 和 当前问题的关注者信息
        return QaUtil.getJSONString(ret ? 0 : 1, info);

    }

    @GetMapping(value = {"/user/{uid}/followees"})
    public String followees(Model model,
                            @PathVariable("uid") int userId) {
        // 需要获取该用户的关注人数，以及每个用户的信息
        List<Integer> ids = followService.getFollowees(EntityType.ENTITY_USER, userId, 0, 10);
        int localUserId = 0;
        if (hostHolder.getUser() != null) {
            localUserId = hostHolder.getUser().getId();
        }
        List<ViewObject> vos = getUsersInfo(localUserId, ids);

        model.addAttribute("followees", vos);
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.selectById(userId));

        // 返回followees页面
        return "followees";
    }

    @GetMapping(value = {"/user/{uid}/followers"})
    public String followers(Model model,
                            @PathVariable("uid") int userId) {
        List<Integer> ids = followService.getFollowers(userId, EntityType.ENTITY_USER, 0, 10);

        int localUserId = 0;
        if (hostHolder.getUser() != null) {
            localUserId = hostHolder.getUser().getId();
        }
        List<ViewObject> vos = getUsersInfo(localUserId, ids);
        /**
         * followers: uid对应的用户的所有粉丝 -> user \ followerCount \ followeeCount \ followed
         * followerCount:uid对应的用户的所有粉丝数
         * curUser: uid对应的用户
         */
        model.addAttribute("followers", vos);
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.selectById(userId));
        return "followers";
    }

    /**
     * 通过id获取每个用户的信息（user, followerCount, followeeCount, 以及当前用户是否关注了该用户）
     * @param localUserId
     * @param userIds
     * @return 用户信息List
     */
    public List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.selectById(uid);
            if (user == null) {
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("user", user);
            // 粉丝数
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));

            // 关注数
            vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, uid));

            // 评论数
            vo.set("commentCount", commentService.getUserCommentCount(uid));

            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));

            } else {
                vo.set("followed", false);
            }

            userInfos.add(vo);
        }
        return userInfos;
    }


}
