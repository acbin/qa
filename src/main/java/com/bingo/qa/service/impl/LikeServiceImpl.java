package com.bingo.qa.service.impl;

import com.bingo.qa.service.LikeService;
import com.bingo.qa.util.JedisAdapter;
import com.bingo.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bingo
 */
@Service
public class LikeServiceImpl implements LikeService {

    private final JedisAdapter jedisAdapter;

    @Autowired
    public LikeServiceImpl(JedisAdapter jedisAdapter) {
        this.jedisAdapter = jedisAdapter;
    }

    /**
     * 当前登录用户点赞某一实体(实体：EntityType + EntityId)
     * 实体作为key，userId作为value，添加进like set集合，同时移除disLike集合中的userId
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, userId + "");

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, userId + "");

        return jedisAdapter.scard(likeKey);
    }


    /**
     * 当前登录用户点踩某一实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public long disLike(int userId, int entityType, int entityId) {
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, userId + "");

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, userId + "");

        return jedisAdapter.scard(likeKey);
    }

    /**
     * 返回用户对实体的喜欢状态(1:赞 -1:踩 0:不赞不踩)
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, userId + "")) {
            return 1;
        }

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey, userId + "") ? -1 : 0;

    }

    /**
     * 返回某实体的点赞数
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }
}