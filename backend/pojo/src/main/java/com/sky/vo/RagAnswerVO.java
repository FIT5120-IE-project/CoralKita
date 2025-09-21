package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * RAG问答响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RagAnswerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户问题
     */
    private String question;

    /**
     * AI生成的答案
     */
    private String answer;

    /**
     * 参考来源信息
     */
    private List<SourceInfo> sources;

    /**
     * 处理时间（毫秒）
     */
    private Long processingTime;

    /**
     * 是否与珊瑚相关
     */
    private Boolean isCoralRelated;

    /**
     * 来源信息内部类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceInfo implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * 来源文件名
         */
        private String sourceFile;

        /**
         * 年份
         */
        private String year;

        /**
         * 相似度分数
         */
        private Double score;

        /**
         * 文档片段内容（截取前200字符）
         */
        private String content;
    }
}
