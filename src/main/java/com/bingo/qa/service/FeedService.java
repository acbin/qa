package com.bingo.qa.service;

import com.bingo.qa.model.Feed;

import java.util.List;

/**
 * Created by bingo on 2018/8/11.
 */

public interface FeedService {
    List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);

    boolean addFeed(Feed feed);

    Feed getFeedById(int id);
}
