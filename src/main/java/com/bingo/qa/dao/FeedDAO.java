package com.bingo.qa.dao;


import com.bingo.qa.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    /**
     * 添加新鲜事
     * @param feed
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId}, #{data}, #{createdDate}, #{type})"})
    int addFeed(Feed feed);


    /**
     * 根据id获取新鲜事
     * @param id
     * @return Feed
     */
    @Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    Feed getFeedById(@Param("id") int id);


    /**
     * 获取用户所关注的人的所有新鲜事
     * @param maxId
     * @param userIds 用户关注的人的id(长度为0时，查出所有)
     * @param count
     * @return List<Feed>
     */
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);

}
