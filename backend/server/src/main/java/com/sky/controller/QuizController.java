package com.sky.controller;

import com.sky.entity.QuizQuestion;
import com.sky.result.Result;
import com.sky.service.QuizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测验题目管理接口
 */
@RestController
@RequestMapping("/quiz")
@Slf4j
@Api(tags = "测验题目相关接口")
public class QuizController {

    @Autowired
    private QuizService quizService;

    /**
     * 根据来源标题随机获取5条测验题目
     * @param sourceTitle 来源标题
     * @return 测验题目列表
     */
    @GetMapping("/questions")
    @ApiOperation(value = "根据来源标题随机获取5条测验题目")
    public Result<List<QuizQuestion>> getRandomQuestions(@RequestParam String sourceTitle) {
        log.info("获取随机测验题目，来源标题：{}", sourceTitle);
        
        // 参数验证
        if (sourceTitle == null || sourceTitle.trim().isEmpty()) {
            return Result.error("来源标题不能为空");
        }
        
        // 查询随机题目
        List<QuizQuestion> questions = quizService.getRandomQuestionsBySourceTitle(sourceTitle);
        
        // 检查是否找到数据
        if (questions.isEmpty()) {
            return Result.error("未找到该来源的测验题目");
        }

        return Result.success(questions);
    }

    /**
     * 获取所有可用的来源标题
     * @return 来源标题列表
     */
    @GetMapping("/sources")
    @ApiOperation(value = "获取所有可用的来源标题")
    public Result<List<String>> getAllSourceTitles() {
        log.info("获取所有可用的来源标题");
        
        List<String> sourceTitles = quizService.getAllSourceTitles();
        
        return Result.success(sourceTitles);
    }
}
