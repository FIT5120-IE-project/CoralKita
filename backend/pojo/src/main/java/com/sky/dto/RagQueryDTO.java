package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * RAG Q&A Query Request DTO
 */
@Data
public class RagQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User Question
     */
    private String question;

    /**
     * User ID (optional, for recording query history)
     */
    private Integer userId;

    /**
     * Number of Results to Return (default 5)
     */
    private Integer topK = 5;

    /**
     * Whether to Include Source Information (default true)
     */
    private Boolean includeSources = true;
}
