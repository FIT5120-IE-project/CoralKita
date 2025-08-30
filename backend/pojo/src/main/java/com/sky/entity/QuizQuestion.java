package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 测验题目实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 来源标题
     */
    private String sourceTitle;

    /**
     * 来源URL
     */
    private String sourceUrl;

    /**
     * 问题内容
     */
    private String question;

    /**
     * 选项A
     */
    private String optionA;

    /**
     * 选项B
     */
    private String optionB;

    /**
     * 选项C
     */
    private String optionC;

    /**
     * 选项D
     */
    private String optionD;

    /**
     * 正确答案
     */
    private String correctOption;

    /**
     * 解释说明
     */
    private String explanation;
}
