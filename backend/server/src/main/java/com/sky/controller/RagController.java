package com.sky.controller;

import com.sky.dto.RagQueryDTO;
import com.sky.result.Result;
import com.sky.service.RagService;
import com.sky.vo.RagAnswerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * RAG Q&A Controller
 */
@RestController
@RequestMapping("/rag")
@Slf4j
@Api(tags = "RAG Intelligent Q&A Related APIs")
public class RagController {

    @Autowired
    private RagService ragService;

    /**
     * RAG intelligent Q&A
     * @param queryDTO Query request
     * @return Q&A result
     */
    @PostMapping("/query")
    @ApiOperation("RAG intelligent Q&A")
    public Result<RagAnswerVO> query(@RequestBody RagQueryDTO queryDTO) {
        log.info("Received RAG Q&A request: {}", queryDTO.getQuestion());
        
        try {
            RagAnswerVO result = ragService.query(queryDTO);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            log.warn("RAG Q&A request parameter error: {}", e.getMessage());
            return Result.error("Request parameter error: " + e.getMessage());
        } catch (Exception e) {
            log.error("RAG Q&A processing failed", e);
            return Result.error("RAG Q&A service temporarily unavailable, please try again later");
        }
    }

    /**
     * Check RAG system status
     * @return System status
     */
    @GetMapping("/status")
    @ApiOperation("Check RAG system status")
    public Result<Boolean> getStatus() {
        log.info("Checking RAG system status");
        
        boolean isReady = ragService.isSystemReady();
        if (isReady) {
            return Result.success(true);
        } else {
            return Result.error("RAG system not ready, please check Python environment and database");
        }
    }

    /**
     * Quick Q&A interface (simplified version)
     * @param question Question
     * @return Answer
     */
    @GetMapping("/quick-query")
    @ApiOperation("Quick Q&A")
    public Result<String> quickQuery(@RequestParam String question) {
        log.info("Received quick Q&A request: {}", question);
        
        try {
            RagQueryDTO queryDTO = new RagQueryDTO();
            queryDTO.setQuestion(question);
            queryDTO.setTopK(3);
            queryDTO.setIncludeSources(false);
            
            RagAnswerVO result = ragService.query(queryDTO);
            return Result.success(result.getAnswer());
        } catch (Exception e) {
            log.error("Quick Q&A processing failed", e);
            return Result.error("Q&A service temporarily unavailable");
        }
    }
}
