package com.sky.service.impl;

import com.sky.entity.QuizQuestion;
import com.sky.mapper.QuizMapper;
import com.sky.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测验服务实现类
 */
@Service
@Slf4j
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizMapper quizMapper;

    /**
     * 根据来源标题随机获取5条测验题目
     * @param sourceTitle 来源标题
     * @return 测验题目列表
     */
    @Override
    public List<QuizQuestion> getRandomQuestionsBySourceTitle(String sourceTitle) {
        log.info("根据来源标题随机查询测验题目：{}", sourceTitle);
        return quizMapper.getRandomQuestionsBySourceTitle(sourceTitle);
    }

    /**
     * 获取所有可用的来源标题
     * @return 来源标题列表
     */
    @Override
    public List<String> getAllSourceTitles() {
        log.info("查询所有可用的来源标题");
        return quizMapper.getAllSourceTitles();
    }
}
