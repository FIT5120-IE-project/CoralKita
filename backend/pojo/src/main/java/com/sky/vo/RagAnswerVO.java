package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * RAG Q&A Response VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RagAnswerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User Question
     */
    private String question;

    /**
     * AI Generated Answer
     */
    private String answer;

    /**
     * Reference Source Information
     */
    private List<SourceInfo> sources;

    /**
     * Processing Time (milliseconds)
     */
    private Long processingTime;

    /**
     * Whether Related to Coral
     */
    private Boolean isCoralRelated;

    /**
     * Source Information Inner Class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceInfo implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * Source File Name
         */
        private String sourceFile;

        /**
         * Year
         */
        private String year;

        /**
         * Similarity Score
         */
        private Double score;

        /**
         * Document Fragment Content (first 200 characters)
         */
        private String content;
    }
}
