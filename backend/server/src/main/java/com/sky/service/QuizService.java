package com.sky.service;

import com.sky.entity.QuizQuestion;

import java.util.List;

/**
 * 测验服务接口
 */
public interface QuizService {

    /**
     * 根据来源标题随机获取5条测验题目
     * @param sourceTitle 来源标题
     * @return 测验题目列表
     */
    List<QuizQuestion> getRandomQuestionsBySourceTitle(String sourceTitle);

    /**
     * 获取所有可用的来源标题
     * @return 来源标题列表
     */
    List<String> getAllSourceTitles();

    /**
     * 从所有来源中随机获取指定数量的题目
     * @param limit 获取题目数量
     * @return 随机题目列表
     */
    List<QuizQuestion> getRandomQuestionsFromAllSources(int limit);
}
