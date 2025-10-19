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
 * Quiz Question Management Controller
 */
@RestController
@RequestMapping("/quiz")
@Slf4j
@Api(tags = "Quiz Question Related APIs")
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
     * Randomly get 5 quiz questions by source title
     * @param sourceTitle Source title
     * @return List of quiz questions
     */
    @GetMapping("/questions")
    @ApiOperation(value = "Randomly get 5 quiz questions by source title")
    public Result<List<QuizQuestion>> getRandomQuestions(@RequestParam String sourceTitle) {
        log.info("Getting random quiz questions, source title: {}", sourceTitle);
        
        // Parameter validation
        if (sourceTitle == null || sourceTitle.trim().isEmpty()) {
            return Result.error("Source title cannot be empty");
        }
        
        // Query random questions
        List<QuizQuestion> questions = quizService.getRandomQuestionsBySourceTitle(sourceTitle);
        
        // Check if data is found
        if (questions.isEmpty()) {
            return Result.error("No quiz questions found for this source");
        }

        return Result.success(questions);
    }

    /**
     * Get all available source titles
     * @return List of source titles
     */
    @GetMapping("/sources")
    @ApiOperation(value = "Get all available source titles")
    public Result<List<String>> getAllSourceTitles() {
        log.info("Getting all available source titles");
        
        List<String> sourceTitles = quizService.getAllSourceTitles();
        
        return Result.success(sourceTitles);
    }

    /**
     * Randomly get 5 questions
     * @return List of random questions
     */
    @GetMapping("/random")
    @ApiOperation(value = "Randomly get 5 questions")
    public Result<List<QuizQuestion>> getRandomQuestions() {
        log.info("Randomly getting 5 questions");
        
        try {
            // Randomly get 5 questions from all sources
            List<QuizQuestion> questions = quizService.getRandomQuestionsFromAllSources(5);
            
            // Check if data is found
            if (questions.isEmpty()) {
                return Result.error("No questions available");
            }

            return Result.success(questions);
        } catch (Exception e) {
            log.error("Failed to get random questions: {}", e.getMessage());
            return Result.error("Failed to get questions: " + e.getMessage());
        }
    }

    /**
     * Submit quiz score
     * @param quizScoreDTO Quiz score information
     * @return Score result
     */
    @PostMapping("/submit-score")
    @ApiOperation(value = "Submit quiz score (logged in)")
    public Result<QuizScoreVO> submitQuizScore(@RequestBody QuizScoreDTO quizScoreDTO) {
        log.info("Submitting quiz score: {}", quizScoreDTO);
        
        // Parameter validation
        if (quizScoreDTO == null) {
            return Result.error("Score information cannot be empty");
        }
        
        if (quizScoreDTO.getUserName() == null || quizScoreDTO.getUserName().trim().isEmpty()) {
            return Result.error("Username cannot be empty");
        }
        
        if (quizScoreDTO.getScore() == null || quizScoreDTO.getScore() < 0) {
            return Result.error("Score cannot be empty or negative");
        }
        

        
        try {
            // Query user information
            User user = userMapper.getByUsername(quizScoreDTO.getUserName());
            if (user == null) {
                return Result.error("User does not exist");
            }

            // Calculate earned experience and points (based on score, assuming max score is 5)
            int earnedExperience = calculateEarnedExperience(quizScoreDTO.getScore());
            int earnedPoints = calculateEarnedPoints(quizScoreDTO.getScore());

            // Update user information
            int newExp = user.getExp() + earnedExperience;
            int newPoints = user.getPoints() + earnedPoints;
            String newLevel = calculateLevel(newExp);

            user.setExp(newExp);
            user.setPoints(newPoints);
            user.setLevel(newLevel);

            // Update database
            userMapper.updateUserScore(user);

            // Build return result
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
            log.error("Failed to process quiz score: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * Calculate earned experience
     * @param score Score
     * @return Earned experience
     */
    private int calculateEarnedExperience(int score) {
        // Base experience: score * 2
        int baseExperience = score * 2;
        
        // Perfect bonus: if score is 5, additional reward
        int perfectBonus = score >= 5 ? 10 : 0;
        
        return baseExperience + perfectBonus;
    }

    /**
     * Calculate earned points
     * @param score Score
     * @return Earned points
     */
    private int calculateEarnedPoints(int score) {
        // Base points: score
        int basePoints = score;
        
        // Perfect bonus: if score is 100, additional reward
        int perfectBonus = score >= 5 ? 5 : 0;
        
        return basePoints + perfectBonus;
    }

    /**
     * Calculate level based on experience
     * @param experience Experience value
     * @return Level name
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
     * Get random coral pictures for quiz
     * @param userName Username
     * @return List of random pictures
     */
    @GetMapping("/coral-pictures")
    @ApiOperation(value = "Get 5 coral pictures for quiz")
    public Result<List<CoralPictureVO>> getRandomCoralPictures(@RequestParam String userName) {
        log.info("Getting random coral pictures, user: {}", userName);
        
        // Parameter validation
        if (userName == null || userName.trim().isEmpty()) {
            return Result.error("Username cannot be empty");
        }
        
        try {
            // Check if user exists
            User user = userMapper.getByUsername(userName);
            if (user == null) {
                return Result.error("User does not exist");
            }
            
            // Randomly get 5 pictures from database
            List<CoralPicture> pictures = coralPictureMapper.selectRandomPictures(5);
            
            if (pictures.isEmpty()) {
                return Result.error("No pictures available");
            }
            
            // Convert to VO and generate signed URLs
            List<CoralPictureVO> pictureVOs = new ArrayList<>();
            for (CoralPicture picture : pictures) {
                // Extract objectKey from original URL
                String originalUrl = picture.getPicture();
                String objectKey = extractObjectKeyFromUrl(originalUrl);
                
                // Generate signed URL
                String signedUrl = ossService.generateSignedUrl(objectKey, 3600);
                
                CoralPictureVO vo = CoralPictureVO.builder()
                        .pictureUrl(signedUrl)
                        .answer(picture.getAnswer())
                        .build();
                pictureVOs.add(vo);
            }
            
            return Result.success(pictureVOs);
        } catch (Exception e) {
            log.error("Failed to get random coral pictures: {}", e.getMessage());
            return Result.error("Failed to get pictures: " + e.getMessage());
        }
    }

    /**
     * Get specified number of healthy and bleached coral pictures for quiz
     * @param healthCount Number of healthy coral pictures (default 4)
     * @param bleachCount Number of bleached coral pictures (default 2)
     * @return List of coral pictures
     */
    @GetMapping("/coral-pictures-balanced")
    @ApiOperation(value = "Get specified number of healthy and bleached coral pictures")
    public Result<List<CoralPictureVO>> getBalancedCoralPictures(
            @RequestParam(defaultValue = "4") int healthCount,
            @RequestParam(defaultValue = "2") int bleachCount) {
        log.info("Getting balanced coral picture combination ({} healthy + {} bleached)", healthCount, bleachCount);
        
        // Parameter validation
        if (healthCount < 0 || bleachCount < 0) {
            return Result.error("Picture count cannot be negative");
        }
        if (healthCount + bleachCount == 0) {
            return Result.error("At least 1 picture is required");
        }
        if (healthCount + bleachCount > 20) {
            return Result.error("Maximum 20 pictures can be retrieved at once");
        }
        
        try {
            // Get healthy coral pictures
            List<CoralPicture> healthPictures = coralPictureMapper.selectByAnswer("health");
            if (healthPictures.size() < healthCount) {
                log.warn("Insufficient healthy coral pictures, requested {}, only {} available", healthCount, healthPictures.size());
                healthCount = healthPictures.size(); // Adjust to actual available count
            }
            
            // Get bleached coral pictures
            List<CoralPicture> bleachPictures = coralPictureMapper.selectByAnswer("bleach");
            if (bleachPictures.size() < bleachCount) {
                log.warn("Insufficient bleached coral pictures, requested {}, only {} available", bleachCount, bleachPictures.size());
                bleachCount = bleachPictures.size(); // Adjust to actual available count
            }
            
            // Randomly select specified number of pictures
            List<CoralPicture> selectedHealthPictures = selectRandomFromList(healthPictures, healthCount);
            List<CoralPicture> selectedBleachPictures = selectRandomFromList(bleachPictures, bleachCount);
            
            // Merge picture lists
            List<CoralPicture> allPictures = new ArrayList<>();
            allPictures.addAll(selectedHealthPictures);
            allPictures.addAll(selectedBleachPictures);
            
            if (allPictures.isEmpty()) {
                return Result.error("No coral pictures available");
            }
            
            // Convert to VO and generate signed URLs
            List<CoralPictureVO> pictureVOs = new ArrayList<>();
            for (CoralPicture picture : allPictures) {
                // Extract objectKey from original URL
                String originalUrl = picture.getPicture();
                String objectKey = extractObjectKeyFromUrl(originalUrl);
                
                // Generate signed URL
                String signedUrl = ossService.generateSignedUrl(objectKey, 3600);
                
                CoralPictureVO vo = CoralPictureVO.builder()
                        .pictureUrl(signedUrl)
                        .answer(picture.getAnswer())
                        .build();
                pictureVOs.add(vo);
            }
            
            log.info("Successfully retrieved {} coral pictures ({} healthy, {} bleached)", 
                    pictureVOs.size(), selectedHealthPictures.size(), selectedBleachPictures.size());
            
            return Result.success(pictureVOs);
        } catch (Exception e) {
            log.error("Failed to get balanced coral pictures: {}", e.getMessage());
            return Result.error("Failed to get pictures: " + e.getMessage());
        }
    }

    /**
     * Randomly select specified number of elements from list
     * @param list Source list
     * @param count Number to select
     * @return List of randomly selected elements
     */
    private List<CoralPicture> selectRandomFromList(List<CoralPicture> list, int count) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        
        if (count >= list.size()) {
            return new ArrayList<>(list);
        }
        
        // Create copy and shuffle randomly
        List<CoralPicture> shuffledList = new ArrayList<>(list);
        java.util.Collections.shuffle(shuffledList);
        
        // Return first count elements
        return shuffledList.subList(0, count);
    }

    /**
     * Extract objectKey from OSS URL
     * @param url OSS URL
     * @return objectKey
     */
    private String extractObjectKeyFromUrl(String url) {
        try {
            // Example URL: https://bucket-name.endpoint.com/image/health/filename.jpg
            // Need to extract: image/health/filename.jpg
            String[] parts = url.split("/");
            if (parts.length >= 4) {
                // Concatenate from 4th part onwards
                StringBuilder objectKey = new StringBuilder();
                for (int i = 3; i < parts.length; i++) {
                    if (i > 3) {
                        objectKey.append("/");
                    }
                    objectKey.append(parts[i]);
                }
                return objectKey.toString();
            }
            return url; // If parsing fails, return original URL
        } catch (Exception e) {
            log.warn("Unable to extract objectKey from URL: {}", url);
            return url;
        }
    }
}
