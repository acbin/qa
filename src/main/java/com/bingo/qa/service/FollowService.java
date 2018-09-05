package com.bingo.qa.service;

import java.util.List;
import java.util.Set;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface FollowService {
    /**
     * 关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean follow(int userId, int entityType, int entityId);

    /**
     * 取消关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean unfollow(int userId, int entityType, int entityId);

    /**
     * 获取粉丝列表（不带偏移）
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    List<Integer> getFollowers(int entityType, int entityId, int count);

    /**
     * 获取粉丝列表（带偏移）
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    List<Integer> getFollowers(int entityType, int entityId, int offset, int count);

    /**
     * 获取用户所关注的用户列表（带偏移）
     * @param entityType
     * @param userId
     * @param count
     * @return
     */
    List<Integer> getFollowees(int entityType, int userId, int count);

    /**
     * 获取用户所关注的用户列表（不带偏移）
     * @param entityType
     * @param userId
     * @param offset
     * @param count
     * @return
     */
    List<Integer> getFollowees(int entityType, int userId, int offset, int count);

    /**
     * 获取某实体的粉丝数
     * @param entityType
     * @param entityId
     * @return
     */
    long getFollowerCount(int entityType, int entityId);

    /**
     * 获取某用户关注的用户数
     * @param userId
     * @param entityType
     * @return
     */
    long getFolloweeCount(int userId, int entityType);

    /**
     * 判断是否是粉丝
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean isFollower(int userId, int entityType, int entityId);
}
