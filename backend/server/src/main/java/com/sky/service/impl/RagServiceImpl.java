package com.sky.service.impl;

import com.sky.dto.RagQueryDTO;
import com.sky.service.RagHttpService;
import com.sky.service.RagService;
import com.sky.vo.RagAnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * RAG Q&A Service Implementation
 */
@Service
@Slf4j
public class RagServiceImpl implements RagService {

    @Autowired
    private RagHttpService ragHttpService;

    @Override
    public RagAnswerVO query(RagQueryDTO queryDTO) {
        log.info("Processing RAG Q&A request: {}", queryDTO.getQuestion());
        
        // Parameter validation
        if (queryDTO.getQuestion() == null || queryDTO.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question cannot be empty");
        }
        
        // Set default values
        if (queryDTO.getTopK() == null || queryDTO.getTopK() <= 0) {
            queryDTO.setTopK(5);
        }
        if (queryDTO.getIncludeSources() == null) {
            queryDTO.setIncludeSources(true);
        }
        
        try {
            // Call RAG HTTP service
            RagAnswerVO result = ragHttpService.queryRag(queryDTO);
            
            // Log query
            log.info("RAG Q&A completed, question: {}, answer length: {}", 
                queryDTO.getQuestion(), 
                result.getAnswer().length());
            
            return result;
            
        } catch (Exception e) {
            log.error("RAG Q&A processing failed", e);
            if (e.getMessage() != null && e.getMessage().contains("Cannot connect to RAG service")) {
                throw new RuntimeException("RAG HTTP service connection failed, please ensure RAG service is started", e);
            } else {
                throw new RuntimeException("RAG Q&A service temporarily unavailable, please try again later", e);
            }
        }
    }

    @Override
    public boolean isSystemReady() {
        try {
            // Check if RAG HTTP service is available
            boolean isHealthy = ragHttpService.checkHealth();
            if (isHealthy) {
                log.info("RAG HTTP service health check passed");
                return true;
            } else {
                log.warn("RAG HTTP service health check failed");
                return false;
            }
            
        } catch (Exception e) {
            log.error("RAG system status check failed", e);
            return false;
        }
    }
}
