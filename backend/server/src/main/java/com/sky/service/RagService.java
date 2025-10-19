package com.sky.service;

import com.sky.dto.RagQueryDTO;
import com.sky.vo.RagAnswerVO;

/**
 * RAG Q&A Service Interface
 */
public interface RagService {
    
    /**
     * Process RAG Q&A request
     * @param queryDTO Query request
     * @return Q&A result
     */
    RagAnswerVO query(RagQueryDTO queryDTO);
    
    /**
     * Check RAG system status
     * @return Whether system is available
     */
    boolean isSystemReady();
}
