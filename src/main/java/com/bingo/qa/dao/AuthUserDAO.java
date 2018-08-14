package com.bingo.qa.dao;

import com.bingo.qa.model.AuthUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author bingo
 * @since 2018/8/14
 */

@Mapper
public interface AuthUserDAO {
    String TABLE_NAME = " auth_user ";
    String INSERT_FIELDS = " user_id ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    /**
     * 添加授权用户
     * @param userId
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{userId})"})
    public int addAuthUser(@Param("userId") int userId);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where user_id = #{userId}"})
    public AuthUser selectAuthUser(@Param("userId") int userId);
}
