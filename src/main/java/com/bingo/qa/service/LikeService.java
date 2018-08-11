package com.bingo.qa.service;

/**
 * Created by bingo on 2018/8/11.
 */

public interface LikeService {
    long like(int userId, int entityType, int entityId);
    long disLike(int userId, int entityType, int entityId);
    int getLikeStatus(int userId, int entityType, int entityId);
    long getLikeCount(int entityType, int entityId);
}
