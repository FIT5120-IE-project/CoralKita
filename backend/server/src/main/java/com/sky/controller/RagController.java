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
 * RAG问答控制器
 */
@RestController
@RequestMapping("/rag")
@Slf4j
@Api(tags = "RAG智能问答相关接口")
public class RagController {

    @Autowired
    private RagService ragService;

    /**
     * RAG智能问答
     * @param queryDTO 查询请求
     * @return 问答结果
     */
    @PostMapping("/query")
    @ApiOperation("RAG智能问答")
    public Result<RagAnswerVO> query(@RequestBody RagQueryDTO queryDTO) {
        log.info("收到RAG问答请求: {}", queryDTO.getQuestion());
        
        try {
            RagAnswerVO result = ragService.query(queryDTO);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            log.warn("RAG问答请求参数错误: {}", e.getMessage());
            return Result.error("请求参数错误: " + e.getMessage());
        } catch (Exception e) {
            log.error("RAG问答处理失败", e);
            return Result.error("RAG问答服务暂时不可用，请稍后重试");
        }
    }

    /**
     * 检查RAG系统状态
     * @return 系统状态
     */
    @GetMapping("/status")
    @ApiOperation("检查RAG系统状态")
    public Result<Boolean> getStatus() {
        log.info("检查RAG系统状态");
        
        boolean isReady = ragService.isSystemReady();
        if (isReady) {
            return Result.success(true);
        } else {
            return Result.error("RAG系统未就绪，请检查Python环境和数据库");
        }
    }

    /**
     * 快速问答接口（简化版）
     * @param question 问题
     * @return 回答
     */
    @GetMapping("/quick-query")
    @ApiOperation("快速问答")
    public Result<String> quickQuery(@RequestParam String question) {
        log.info("收到快速问答请求: {}", question);
        
        try {
            RagQueryDTO queryDTO = new RagQueryDTO();
            queryDTO.setQuestion(question);
            queryDTO.setTopK(3);
            queryDTO.setIncludeSources(false);
            
            RagAnswerVO result = ragService.query(queryDTO);
            return Result.success(result.getAnswer());
        } catch (Exception e) {
            log.error("快速问答处理失败", e);
            return Result.error("问答服务暂时不可用");
        }
    }
}
