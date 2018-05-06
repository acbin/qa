package com.bingo.qa.util;


import com.alibaba.fastjson.JSONObject;
import com.bingo.qa.model.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * 测试redis方法
 */
public class JedisTest {

    public static void main(String[] args) {

        Jedis jedis = new Jedis("redis://localhost:6379/0");
        jedis.flushDB();
        jedis.set("hello", "bingo");
        System.out.println(jedis.get("hello"));
        jedis.rename("hello" , "newhello");


        // 设置过期时间
        // 应用：验证码放redis，设置过期时间，下发给用户
        jedis.setex("hello2", 15, "world");


        jedis.set("pv", "100");
        jedis.incr("pv");
        System.out.println(jedis.get("pv"));
        jedis.incrBy("pv", 5);
        System.out.println(jedis.get("pv"));

        jedis.decr("pv");


        // list : 类似栈(应用：可以做最近关注的人)
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; ++i) {
            jedis.lpush(listName, "a" + i);
        }
        System.out.println(jedis.lrange(listName, 0 ,3));

        System.out.println(jedis.llen(listName));
        System.out.println(jedis.lpop(listName));
        System.out.println(jedis.llen(listName));

        System.out.println(jedis.lrange(listName, 0, 6));
        System.out.println(jedis.lindex(listName, 0));
        System.out.println(jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a8", "bb"));
        System.out.println(jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a8", "cc"));

        System.out.println(jedis.lrange(listName, 0, jedis.llen(listName) - 1));


        // hash
        String userKey = "user1";

        // 增加
        jedis.hset(userKey, "name", "bingo");
        jedis.hset(userKey, "age", "11");
        jedis.hset(userKey, "phone", "15888888888");

        // 获取
        System.out.println(jedis.hget(userKey, "name"));
        System.out.println(jedis.hgetAll(userKey));

        // 删除
        System.out.println(jedis.hdel(userKey, "age"));
        System.out.println(jedis.hgetAll(userKey));

        jedis.hsetnx(userKey, "school", "szu");
        jedis.hsetnx(userKey, "name", "yy");
        System.out.println(jedis.hgetAll(userKey));




        // set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, "" + i);
            jedis.sadd(likeKey2, "" + i * i);
        }

        System.out.println(jedis.smembers(likeKey1));
        System.out.println(jedis.smembers(likeKey2));

        //求并
        System.out.println(jedis.sunion(likeKey1, likeKey2));

        // 求差集
        System.out.println(jedis.sdiff(likeKey1, likeKey2));

        // 求交
        System.out.println(jedis.sinter(likeKey1, likeKey2));

        // 查看是否有
        System.out.println(jedis.sismember(likeKey1, "12"));

        // 移动
        jedis.smove(likeKey2, likeKey1, "25");
        System.out.println(jedis.smembers(likeKey1));

        // 集合有多少个元素
        System.out.println(jedis.scard(likeKey1));

        // 随机取值
        System.out.println(jedis.srandmember(likeKey1));

        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 100, "Ben");
        jedis.zadd(rankKey, 80, "Lee");

        // 求区间内的个数
        System.out.println(jedis.zcount(rankKey, 61, 100));

        System.out.println(jedis.zscore(rankKey, "Ben"));


        // 带权
        jedis.zincrby(rankKey, 3,  "Lee");
        System.out.println(jedis.zscore(rankKey, "Lee"));

        System.out.println(jedis.zrevrange(rankKey, 0 ,3));

        // 根据分值排序
        for (Tuple tuple : jedis.zrevrangeByScoreWithScores(rankKey, 100, 60)) {
            System.out.println(tuple.getElement() + ": " + String.valueOf(tuple.getScore()));
        }


        // 降序排名
        System.out.println(jedis.zrevrank(rankKey, "Ben"));

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        System.out.println(jedis.zlexcount(setKey, "[a", "(b"));

        jedis.zrem(setKey, "b");


        jedis.zremrangeByLex(setKey, "(c", "+");
        jedis.zrange(setKey, 0 ,2);

        // 不是记住这些命令，方法，而是知道底层用的是什么结构，什么场景下需要用什么结构

        JedisPool pool = new JedisPool();
        for (int i = 0; i < 100; ++i) {
            Jedis j = pool.getResource();
            System.out.println(j.get("pv"));
            j.close();
        }


        User user = new User();
        user.setName("xxx");
        user.setPassword("ppp");
        user.setHeadUrl("xxxx");
        user.setSalt("salt");
        user.setId(1);

        // 将对象转为Json串存到缓存中
        System.out.println(JSONObject.toJSONString(user));
        jedis.set("user1", JSONObject.toJSONString(user));

        // 将Json串转为对象
        String value = jedis.get("user1");
        User user2 = JSONObject.parseObject(value, User.class);





    }
}
