package com.bingo.qa.util;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";

    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    // 实体下的粉丝(关注者)
    private static String BIZ_FOLLOWER = "FOLLOWER";

    // 用户所关注的实体(关注对象)
    private static String BIZ_FOLLOWEE = "FOLLOWEE";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    
    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    /**
     * 每个实体(作为key)，粉丝存入该key对应的值中
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 一个用户，关注了某一类实体,如用户、问题等(由userId与entityType共同组成key)
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

}
