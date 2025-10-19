package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Quiz Score DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizScoreDTO {

    /**
     * Username
     */
    private String userName;

    /**
     * Quiz Score
     */
    private Integer score;

}
