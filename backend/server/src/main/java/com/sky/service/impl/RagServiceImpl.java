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
 * RAG问答服务实现类
 */
@Service
@Slf4j
public class RagServiceImpl implements RagService {

    @Autowired
    private RagHttpService ragHttpService;

    @Override
    public RagAnswerVO query(RagQueryDTO queryDTO) {
        log.info("处理RAG问答请求: {}", queryDTO.getQuestion());
        
        // 参数验证
        if (queryDTO.getQuestion() == null || queryDTO.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("问题不能为空");
        }
        
        // 设置默认值
        if (queryDTO.getTopK() == null || queryDTO.getTopK() <= 0) {
            queryDTO.setTopK(5);
        }
        if (queryDTO.getIncludeSources() == null) {
            queryDTO.setIncludeSources(true);
        }
        
        try {
            // 调用RAG HTTP服务
            RagAnswerVO result = ragHttpService.queryRag(queryDTO);
            
            // 记录查询日志
            log.info("RAG问答完成，问题: {}, 回答长度: {}", 
                queryDTO.getQuestion(), 
                result.getAnswer().length());
            
            return result;
            
        } catch (Exception e) {
            log.error("RAG问答处理失败", e);
            if (e.getMessage().contains("无法连接到RAG服务")) {
                throw new RuntimeException("RAG HTTP服务连接失败，请确保RAG服务已启动", e);
            } else {
                throw new RuntimeException("RAG问答服务暂时不可用，请稍后重试", e);
            }
        }
    }

    @Override
    public boolean isSystemReady() {
        try {
            // 检查RAG HTTP服务是否可用
            boolean isHealthy = ragHttpService.checkHealth();
            if (isHealthy) {
                log.info("RAG HTTP服务健康检查通过");
                return true;
            } else {
                log.warn("RAG HTTP服务健康检查失败");
                return false;
            }
            
        } catch (Exception e) {
            log.error("RAG系统状态检查失败", e);
            return false;
        }
    }
}
