package com.bingo.qa.service;

import java.util.List;
import java.util.Set;

/**
 * Created by bingo on 2018/8/11.
 */

public interface FollowService {
    boolean follow(int userId, int entityType, int entityId);

    boolean unfollow(int userId, int entityType, int entityId);

    List<Integer> getFollowers(int entityType, int entityId, int count);

    List<Integer> getFollowers(int entityType, int entityId, int offset, int count);

    List<Integer> getFollowees(int entityType, int userId, int count);

    List<Integer> getFollowees(int entityType, int userId, int offset, int count);

    long getFollowerCount(int entityType, int entityId);

    long getFolloweeCount(int userId, int entityType);

    boolean isFollower(int userId, int entityType, int entityId);
}
