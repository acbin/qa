package com.bingo.qa.service;

/**
 *
 * @author bingo
 * @since 2018/8/11
 */

public interface LikeService {
    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    long like(int userId, int entityType, int entityId);

    /**
     * 点踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    long disLike(int userId, int entityType, int entityId);

    /**
     * 获取当前点赞状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    int getLikeStatus(int userId, int entityType, int entityId);

    /**
     * 获取点赞数
     * @param entityType
     * @param entityId
     * @return
     */
    long getLikeCount(int entityType, int entityId);
}
