package com.sky.service;

import com.sky.entity.QuizQuestion;

import java.util.List;

/**
 * Quiz Service Interface
 */
public interface QuizService {

    /**
     * Randomly get 5 quiz questions by source title
     * @param sourceTitle Source title
     * @return List of quiz questions
     */
    List<QuizQuestion> getRandomQuestionsBySourceTitle(String sourceTitle);

    /**
     * Get all available source titles
     * @return List of source titles
     */
    List<String> getAllSourceTitles();

    /**
     * Randomly get specified number of questions from all sources
     * @param limit Number of questions to get
     * @return List of random questions
     */
    List<QuizQuestion> getRandomQuestionsFromAllSources(int limit);
}
