package com.sky.mapper;

import com.sky.entity.QuizQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuizMapper {

    /**
     * 根据来源标题随机查询5条测验题目
     * @param sourceTitle 来源标题
     * @return 测验题目列表
     */
    @Select("SELECT * FROM quiz_questions WHERE source_title = #{sourceTitle} ORDER BY RAND() LIMIT 5")
    List<QuizQuestion> getRandomQuestionsBySourceTitle(String sourceTitle);

    /**
     * 查询所有可用的来源标题
     * @return 来源标题列表
     */
    @Select("SELECT DISTINCT source_title FROM quiz_questions WHERE source_title IS NOT NULL")
    List<String> getAllSourceTitles();
}
