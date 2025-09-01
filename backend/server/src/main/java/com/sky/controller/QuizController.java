package com.sky.controller;

import com.sky.entity.QuizQuestion;
import com.sky.entity.User;
import com.sky.entity.CoralPicture;
import com.sky.dto.QuizScoreDTO;
import com.sky.result.Result;
import com.sky.service.QuizService;
import com.sky.service.OssService;
import com.sky.mapper.UserMapper;
import com.sky.mapper.CoralPictureMapper;
import com.sky.vo.QuizScoreVO;
import com.sky.vo.CoralPictureVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

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

    @Autowired
    private CoralPictureMapper coralPictureMapper;

    @Autowired
    private OssService ossService;

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
    @ApiOperation(value = "提交测验得分(已登录)")
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

    /**
     * 获取随机珊瑚图片用于测验
     * @param userName 用户名称
     * @return 随机图片列表
     */
    @GetMapping("/coral-pictures")
    @ApiOperation(value = "获取5张珊瑚图片用于测验")
    public Result<List<CoralPictureVO>> getRandomCoralPictures(@RequestParam String userName) {
        log.info("获取随机珊瑚图片，用户：{}", userName);
        
        // 参数验证
        if (userName == null || userName.trim().isEmpty()) {
            return Result.error("用户名称不能为空");
        }
        
        try {
            // 查询用户是否存在
            User user = userMapper.getByUsername(userName);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 从数据库随机获取5张图片
            List<CoralPicture> pictures = coralPictureMapper.selectRandomPictures(5);
            
            if (pictures.isEmpty()) {
                return Result.error("没有可用的图片");
            }
            
            // 转换为VO并生成签名URL
            List<CoralPictureVO> pictureVOs = new ArrayList<>();
            for (CoralPicture picture : pictures) {
                // 从原始URL中提取objectKey
                String originalUrl = picture.getPicture();
                String objectKey = extractObjectKeyFromUrl(originalUrl);
                
                // 生成签名URL
                String signedUrl = ossService.generateSignedUrl(objectKey, 3600);
                
                CoralPictureVO vo = CoralPictureVO.builder()
                        .pictureUrl(signedUrl)
                        .answer(picture.getAnswer())
                        .build();
                pictureVOs.add(vo);
            }
            
            return Result.success(pictureVOs);
        } catch (Exception e) {
            log.error("获取随机珊瑚图片失败：{}", e.getMessage());
            return Result.error("获取图片失败：" + e.getMessage());
        }
    }

    /**
     * 从OSS URL中提取objectKey
     * @param url OSS URL
     * @return objectKey
     */
    private String extractObjectKeyFromUrl(String url) {
        try {
            // 示例URL: https://bucket-name.endpoint.com/image/health/filename.jpg
            // 需要提取: image/health/filename.jpg
            String[] parts = url.split("/");
            if (parts.length >= 4) {
                // 从第4个部分开始拼接
                StringBuilder objectKey = new StringBuilder();
                for (int i = 3; i < parts.length; i++) {
                    if (i > 3) {
                        objectKey.append("/");
                    }
                    objectKey.append(parts[i]);
                }
                return objectKey.toString();
            }
            return url; // 如果无法解析，返回原URL
        } catch (Exception e) {
            log.warn("无法从URL提取objectKey: {}", url);
            return url;
        }
    }
}
