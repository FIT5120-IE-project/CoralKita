package com.sky.service.impl;

import com.sky.entity.QuizQuestion;
import com.sky.mapper.QuizMapper;
import com.sky.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Quiz Service Implementation
 */
@Service
@Slf4j
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizMapper quizMapper;

    /**
     * Randomly get 5 quiz questions by source title
     * @param sourceTitle Source title
     * @return List of quiz questions
     */
    @Override
    public List<QuizQuestion> getRandomQuestionsBySourceTitle(String sourceTitle) {
        log.info("Randomly querying quiz questions by source title: {}", sourceTitle);
        return quizMapper.getRandomQuestionsBySourceTitle(sourceTitle);
    }

    /**
     * Get all available source titles
     * @return List of source titles
     */
    @Override
    public List<String> getAllSourceTitles() {
        log.info("Querying all available source titles");
        return quizMapper.getAllSourceTitles();
    }

    /**
     * Randomly get specified number of questions from all sources
     * @param limit Number of questions to get
     * @return List of random questions
     */
    @Override
    public List<QuizQuestion> getRandomQuestionsFromAllSources(int limit) {
        log.info("Randomly getting {} questions from all sources", limit);
        return quizMapper.getRandomQuestionsFromAllSources(limit);
    }
}
