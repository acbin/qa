package com.bingo.qa.service.impl;

import com.bingo.qa.service.FollowService;
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
 * Created by bingo on 2018/5/31.
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 关注功能:关注的发起者是用户，关注的实体可以是用户，也可以是问题等..
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return 是否关注成功
     */
    @Override
    public boolean follow(int userId, int entityType, int entityId) {
        // 一个实体下的关注者，该实体作为key，关注者存入该key对应的值
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        // 用户关注的对象
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();

        // 此处开启redis事务，确保数据一致性
        Transaction tx = jedisAdapter.multi(jedis);

        // 某个实体下的粉丝列表(时间戳作为score)
        tx.zadd(followerKey, date.getTime(), userId + "");

        // 某个实体下关注的类型列表
        tx.zadd(followeeKey, date.getTime(), entityId + "");

        // 执行事务内所有操作
        List<Object> ret = jedisAdapter.exec(tx, jedis);

        // 返回结果数量为2，并且每个结果返回值均大于0，则返回true
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 取消关注功能
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return 是否取关成功
     */
    @Override
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
     *
     * @param entityType
     * @param entityId
     * @param count      获取followers数量
     * @return
     */
    @Override
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }


    /**
     * 获取某一实体下最新粉丝关注列表（带offset）
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, count));
    }


    /**
     * 获取某一用户关注的某一类型 的 实体列表（不带offset）
     *
     * @param entityType
     * @param userId
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowees(int entityType, int userId, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    /**
     * 获取某一用户关注的某一类型 的 实体列表（带offset）
     *
     * @param entityType
     * @param userId
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowees(int entityType, int userId, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, count));
    }

    /**
     * 获取某一实体下的关注者数量（eg. 我被x, y, z关注，那么数量为3）
     *
     * @param entityType
     * @param entityId
     * @return 我的粉丝数
     */
    @Override
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    /**
     * 获得当前实体所关注的某一类型实体的数量（eg. 我关注了 a,b,c,d,那么数量为4）
     *
     * @param userId
     * @param entityType
     * @return 我的关注数
     */
    @Override
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    /**
     * 判断用户是否是某一实体的关注者
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        // 每个成员的score越大，表示越是新近关注
        return jedisAdapter.zscore(followerKey, userId + "") != null;
    }
}