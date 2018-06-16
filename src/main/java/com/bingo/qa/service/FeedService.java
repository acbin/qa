package com.bingo.qa.service;

import com.bingo.qa.dao.FeedDAO;
import com.bingo.qa.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bingo on 2018/6/2.
 */

@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;

    /**
     * 拉模式，把关注的所有用户的feed都查找出来
     * @param maxId
     * @param userIds
     * @param count
     * @return list
     */
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed) {
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getFeedById(int id) {
        return feedDAO.getFeedById(id);
    }

}
