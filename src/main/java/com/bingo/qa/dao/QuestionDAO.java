package com.bingo.qa.dao;

import com.bingo.qa.model.Question;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    Question selectById(int id);


    @Update({"update ", TABLE_NAME, " set comment_count = #{comment_count} where id = #{entityId}"})
    int updateCommentCount(@Param("entityId") int entityId,
                           @Param("comment_count") int comment_count);
}
