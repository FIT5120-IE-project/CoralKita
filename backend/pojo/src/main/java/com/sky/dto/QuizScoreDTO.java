package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测验得分DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizScoreDTO {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 测验得分
     */
    private Integer score;

}
