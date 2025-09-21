package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * RAG问答查询请求DTO
 */
@Data
public class RagQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户问题
     */
    private String question;

    /**
     * 用户ID（可选，用于记录查询历史）
     */
    private Integer userId;

    /**
     * 返回结果数量（默认5）
     */
    private Integer topK = 5;

    /**
     * 是否包含来源信息（默认true）
     */
    private Boolean includeSources = true;
}
