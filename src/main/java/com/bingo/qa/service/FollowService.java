package com.bingo.qa.service;

import com.bingo.qa.util.JedisAdapter;
import com.bingo.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by bingo in 2018/5/31.
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 关注功能
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        // 一个实体下的关注者，该实体作为key，关注者存入该key对应的值
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);

        // 某个实体下的粉丝列表
        tx.zadd(followerKey, date.getTime(), userId + "");

        // 某个实体下关注的类型列表
        tx.zadd(followeeKey, date.getTime(), entityId + "");

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;


    }

    /**
     * 取消关注功能
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);

        // 某个实体下的粉丝列表
        tx.zrem(followerKey, userId + "");

        // 某个实体下关注的类型列表
        tx.zrem(followeeKey, entityId + "");

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;

    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }


    /**
     * 获取某一实体下粉丝关注列表（不带offset）
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }


    /**
     * 获取某一实体下粉丝关注列表（带offset）
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, count));
    }



    /**
     * 获取某一用户关注的某一类型 的 实体列表（不带offset）
     * @param entityType
     * @param userId
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int entityType, int userId, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    /**
     * 获取某一用户关注的某一类型 的 实体列表（带offset）
     * @param entityType
     * @param userId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int entityType, int userId, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, count));
    }

    /**
     * 获取某一实体下的关注者数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    /**
     * 判断用户是否是某一实体的关注者
      * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, userId + "") != null;
    }



}
