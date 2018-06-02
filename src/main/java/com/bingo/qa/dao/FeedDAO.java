package com.bingo.qa.dao;


import com.bingo.qa.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId}, #{data}, #{createdDate}, #{type})"})
    public int addFeed(Feed feed);


    @Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    Feed getFeedById(@Param("id") int id);

    // userIds是用户关注的人的id，查找它们所有的新鲜事
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);

}
