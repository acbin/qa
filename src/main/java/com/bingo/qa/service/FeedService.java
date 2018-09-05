package com.bingo.qa.service;

import com.bingo.qa.model.Feed;

import java.util.List;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface FeedService {
    /**
     * 获取feed列表
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);

    /**
     * 添加feed
     * @param feed
     * @return
     */
    boolean addFeed(Feed feed);

    /**
     * 根据id获取feed
     * @param id
     * @return
     */
    Feed getFeedById(int id);
}
