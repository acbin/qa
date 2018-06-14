package com.bingo.qa.dao;

import com.bingo.qa.model.Question;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    /**
     * 添加问题
     * @param question
     * @return
     */
    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);


    /**
     * 根据用户id查找最近发布的问题，若id=0，则查出所有用户最近发布的问题
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);


    /**
     * 根据id查找问题
     * @param id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    Question selectById(int id);


    /**
     * 更新问题所对应的评论数
     * @param entityId
     * @param comment_count
     * @return
     */
    @Update({"update ", TABLE_NAME, " set comment_count = #{comment_count} where id = #{entityId}"})
    int updateCommentCount(@Param("entityId") int entityId,
                           @Param("comment_count") int comment_count);
}
