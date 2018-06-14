package com.bingo.qa.dao;

import com.bingo.qa.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    /**
     * 添加私信
     * @param message
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
    ") values(#{fromId}, #{toId}, #{content}, #{hasRead}, #{conversationId}, #{createdDate})"})
    int addMessage(Message message);


    /**
     * 根据conversationId查找私信列表
     * (根据created_date降序排列)
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
     "where conversation_id = #{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);


    /**
     * 根据userId查找当前登录用户与其他所有人的私信列表
     * (需要将与其他用户的最新一条私信查找出来，并且查找与该用户私信的总条数，可以将总条数封装到message的id中)
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    @Select({"select t1.from_id, t1.to_id, t1.content, t1.has_read, t1.conversation_id, t1.created_date, t2.cnt id" +
            " from message t1, (select count(1) cnt, max(created_date) created_date from message group by conversation_id) as t2" +
            " where t1.created_date = t2.created_date and (from_id = #{userId} or to_id = #{userId})" +
            " order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);


    /**
     * 根据conversationId与用户查找与对方的未读私信数量
     * @param userId
     * @param conversationId
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, "where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);


    /**
     * 更新私信状态
     * @param id
     * @param status
     * @return
     */
    @Update({"update ", TABLE_NAME, " set has_read = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);


    /**
     * 根据id查找私信
     * @param id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    Message getById(int id);
}
