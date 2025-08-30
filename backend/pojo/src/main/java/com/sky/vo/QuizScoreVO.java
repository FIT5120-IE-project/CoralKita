package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测验得分VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizScoreVO {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户等级
     */
    private String level;

    /**
     * 用户经验值
     */
    private Integer experience;

    /**
     * 用户积分
     */
    private Integer points;

    /**
     * 获得的经验值
     */
    private Integer earnedExperience;

    /**
     * 获得的积分
     */
    private Integer earnedPoints;


}
