package com.bingo.qa.dao;


import com.bingo.qa.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author bingo
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId}, #{content}, #{createdDate}, #{entityId}, #{entityType}, #{status})"})
    int addComment(Comment comment);


    /**
     * 获取某实体下的所有评论
     * @param entityId
     * @param entityType
     * @return 评论列表
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);

    /**
     * 获取某实体下的评论数量
     * @param entityId
     * @param entityType
     * @return 评论数
     */
    @Select({"select count(id) from ", TABLE_NAME,
            "where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);


    /**
     * 更新评论状态
     * @param id
     * @param status
     * @return
     */
    @Update({"update ", TABLE_NAME, " set status = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);


    /**
     * 根据id获取评论
     * @param id
     * @return comment
     */
    @Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    Comment getCommentById(@Param("id") int id);


    /**
     * 获取某用户的评论数量
     * @param userId
     * @return 评论数
     */
    @Select({"select count(id) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
