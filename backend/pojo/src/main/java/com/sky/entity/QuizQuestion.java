package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Quiz Question Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key ID
     */
    private Integer id;

    /**
     * Source Title
     */
    private String sourceTitle;

    /**
     * Source URL
     */
    private String sourceUrl;

    /**
     * Question Content
     */
    private String question;

    /**
     * Option A
     */
    private String optionA;

    /**
     * Option B
     */
    private String optionB;

    /**
     * Option C
     */
    private String optionC;

    /**
     * Option D
     */
    private String optionD;

    /**
     * Correct Option
     */
    private String correctOption;

    /**
     * Explanation
     */
    private String explanation;
}
