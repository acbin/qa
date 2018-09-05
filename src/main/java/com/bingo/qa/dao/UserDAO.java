package com.bingo.qa.dao;

import com.bingo.qa.model.User;
import org.apache.ibatis.annotations.*;

/**
 * @author bingo
 */
@Mapper
public interface UserDAO {

    String TABLE_NAME = " user ";
    String INSERT_FIELDS = "name, password, salt, head_url ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    /**
     * 添加用户
     * @param user
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{name}, #{password}, #{salt}, #{headUrl})"})
    void addUser(User user);

    /**
     * 根据id查询用户
     * @param id
     * @return User
     */
    @Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    User selectById(int id);

    /**
     * 更新用户密码
     * @param user
     */
    @Update({"update ", TABLE_NAME, " set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    /**
     * 根据id删除用户
     * @param id
     */
    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteById(int id);

    /**
     * 根据用户名查询用户
     * @param name
     * @return User
     */
    @Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where name = #{name}"})
    User selectByName(String name);

}
