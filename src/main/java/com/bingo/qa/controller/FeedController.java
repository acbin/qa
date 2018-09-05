package com.bingo.qa.controller;

import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.Feed;
import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.User;
import com.bingo.qa.service.FeedService;
import com.bingo.qa.service.FollowService;
import com.bingo.qa.util.JedisAdapter;
import com.bingo.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bingo
 * @since 2018/6/2
 */

@Controller
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 拉模式:取出登录用户所关注的人的动态
     * @param model
     * @return
     */
    @GetMapping(value = {"/pullfeeds"})
    public String getPullFeeds(Model model) {
        User user = hostHolder.getUser();
        int localUserId = 0;
        // 当前处于登录状态
        if (user != null) {
            localUserId = user.getId();
        }

        // 找到用户所有关注的人
        List<Integer> followees = followService.getFollowees(
                EntityType.ENTITY_USER,
                localUserId,
                Integer.MAX_VALUE
        );

        // 取出10条当前用户所关注的用户的feed
        // 如果当前没有处于登录状态，则会取出所有（由list.size()是否等于0决定,在FeedDAO中做判断）
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);

        return "feeds";
    }

    /**
     * 推模式
     * @param model
     * @return
     */
    @GetMapping(value = {"/pushfeeds"})
    public String getPushFeeds(Model model) {
        User user = hostHolder.getUser();
        int localUserId = 0;
        // 当前处于登录状态
        if (user != null) {
            localUserId = user.getId();
        }

        // 取出当前登录用户关注的事件的feedIds
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimeLineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();

        for (String feedId : feedIds) {
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if (feed == null) {
                continue;
            }
            feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);

        return "feeds";
    }

}
