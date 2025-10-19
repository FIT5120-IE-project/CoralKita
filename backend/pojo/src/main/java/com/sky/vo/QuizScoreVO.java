package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Quiz Score VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizScoreVO {

    /**
     * User ID
     */
    private Integer userId;

    /**
     * Username
     */
    private String userName;

    /**
     * User Level
     */
    private String level;

    /**
     * User Experience
     */
    private Integer experience;

    /**
     * User Points
     */
    private Integer points;

    /**
     * Earned Experience
     */
    private Integer earnedExperience;

    /**
     * Earned Points
     */
    private Integer earnedPoints;


}
