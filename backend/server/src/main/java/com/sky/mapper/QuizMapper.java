package com.sky.mapper;

import com.sky.entity.QuizQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuizMapper {

    /**
     * Randomly query 5 quiz questions by source title
     * @param sourceTitle Source title
     * @return List of quiz questions
     */
    @Select("SELECT * FROM quiz_questions WHERE source_title = #{sourceTitle} ORDER BY RAND() LIMIT 5")
    List<QuizQuestion> getRandomQuestionsBySourceTitle(String sourceTitle);

    /**
     * Query all available source titles
     * @return List of source titles
     */
    @Select("SELECT DISTINCT source_title FROM quiz_questions WHERE source_title IS NOT NULL")
    List<String> getAllSourceTitles();

    /**
     * Randomly query specified number of quiz questions from all sources
     * @param limit Query limit
     * @return List of quiz questions
     */
    @Select("SELECT * FROM quiz_questions ORDER BY RAND() LIMIT #{limit}")
    List<QuizQuestion> getRandomQuestionsFromAllSources(int limit);
}
