package com.sky.controller;

import com.sky.entity.QuizQuestion;
import com.sky.entity.User;
import com.sky.dto.QuizScoreDTO;
import com.sky.result.Result;
import com.sky.service.QuizService;
import com.sky.mapper.UserMapper;
import com.sky.vo.QuizScoreVO;
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

    @Autowired
    private UserMapper userMapper;

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

    /**
     * 提交测验得分
     * @param quizScoreDTO 测验得分信息
     * @return 得分结果
     */
    @PostMapping("/submit-score")
    @ApiOperation(value = "提交测验得分")
    public Result<QuizScoreVO> submitQuizScore(@RequestBody QuizScoreDTO quizScoreDTO) {
        log.info("提交测验得分：{}", quizScoreDTO);
        
        // 参数验证
        if (quizScoreDTO == null) {
            return Result.error("得分信息不能为空");
        }
        
        if (quizScoreDTO.getUserName() == null || quizScoreDTO.getUserName().trim().isEmpty()) {
            return Result.error("用户名称不能为空");
        }
        
        if (quizScoreDTO.getScore() == null || quizScoreDTO.getScore() < 0) {
            return Result.error("得分不能为空或负数");
        }
        

        
        try {
            // 查询用户信息
            User user = userMapper.getByUsername(quizScoreDTO.getUserName());
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 计算获得的经验值和积分（基于得分，假设满分5）
            int earnedExperience = calculateEarnedExperience(quizScoreDTO.getScore());
            int earnedPoints = calculateEarnedPoints(quizScoreDTO.getScore());

            // 更新用户信息
            int newExp = user.getExp() + earnedExperience;
            int newPoints = user.getPoints() + earnedPoints;
            String newLevel = calculateLevel(newExp);

            user.setExp(newExp);
            user.setPoints(newPoints);
            user.setLevel(newLevel);

            // 更新数据库
            userMapper.updateUserScore(user);

            // 构建返回结果
            QuizScoreVO quizScoreVO = QuizScoreVO.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .level(newLevel)
                    .experience(newExp)
                    .points(newPoints)
                    .earnedExperience(earnedExperience)
                    .earnedPoints(earnedPoints)
                    .build();

            return Result.success(quizScoreVO);
        } catch (Exception e) {
            log.error("处理测验得分失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 计算获得的经验值
     * @param score 得分
     * @return 获得的经验值
     */
    private int calculateEarnedExperience(int score) {
        // 基础经验值：得分 * 2
        int baseExperience = score * 2;
        
        // 满分奖励：如果得分5，额外奖励
        int perfectBonus = score >= 5 ? 10 : 0;
        
        return baseExperience + perfectBonus;
    }

    /**
     * 计算获得的积分
     * @param score 得分
     * @return 获得的积分
     */
    private int calculateEarnedPoints(int score) {
        // 基础积分：得分
        int basePoints = score;
        
        // 满分奖励：如果得分100，额外奖励
        int perfectBonus = score >= 5 ? 5 : 0;
        
        return basePoints + perfectBonus;
    }

    /**
     * 根据经验值计算等级
     * @param experience 经验值
     * @return 等级名称
     */
    private String calculateLevel(int experience) {
        if (experience < 100) {
            return "Novice";
        } else if (experience < 300) {
            return "Beginner";
        } else if (experience < 600) {
            return "Intermediate";
        } else if (experience < 1000) {
            return "Expert";
        } else if (experience < 2000) {
            return "Master";
        } else {
            return "Legend";
        }
    }


}
