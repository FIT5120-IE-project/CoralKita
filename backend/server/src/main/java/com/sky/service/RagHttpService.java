package com.sky.service;

import com.sky.dto.RagQueryDTO;
import com.sky.vo.RagAnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

/**
 * RAG HTTP服务调用
 */
@Service
@Slf4j
public class RagHttpService {

    @Value("${rag.http.url:http://localhost:5000}")
    private String ragHttpUrl;

    private final RestTemplate restTemplate;

    public RagHttpService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 调用RAG HTTP服务进行问答
     * @param queryDTO 查询请求
     * @return RAG回答结果
     */
    public RagAnswerVO queryRag(RagQueryDTO queryDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("开始调用RAG HTTP服务，问题: {}", queryDTO.getQuestion());
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("question", queryDTO.getQuestion());
            requestBody.put("top_k", queryDTO.getTopK());
            requestBody.put("include_sources", queryDTO.getIncludeSources());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 发送请求
            String url = ragHttpUrl + "/rag/query";
            log.info("发送请求到: {}", url);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                
                // 检查响应是否成功
                Boolean success = (Boolean) responseBody.get("success");
                if (success != null && success) {
                    // 构建返回结果
                    RagAnswerVO result = new RagAnswerVO();
                    result.setAnswer((String) responseBody.get("answer"));
                    result.setIsCoralRelated((Boolean) responseBody.get("is_coral_related"));
                    
                    // 处理来源信息
                    if (queryDTO.getIncludeSources() && responseBody.get("sources") != null) {
                        // 这里需要将sources转换为List<SourceInfo>，暂时设为null
                        result.setSources(null);
                    }
                    
                    long endTime = System.currentTimeMillis();
                    log.info("RAG HTTP服务调用成功，耗时: {}ms", endTime - startTime);
                    
                    return result;
                } else {
                    String error = (String) responseBody.get("error");
                    log.error("RAG HTTP服务返回错误: {}", error);
                    return createErrorResult("RAG服务错误: " + error);
                }
            } else {
                log.error("RAG HTTP服务响应异常，状态码: {}", response.getStatusCode());
                return createErrorResult("RAG服务响应异常");
            }
            
        } catch (RestClientException e) {
            log.error("调用RAG HTTP服务失败: {}", e.getMessage(), e);
            return createErrorResult("无法连接到RAG服务: " + e.getMessage());
        } catch (Exception e) {
            log.error("RAG HTTP服务调用异常: {}", e.getMessage(), e);
            return createErrorResult("RAG服务调用异常: " + e.getMessage());
        }
    }

    /**
     * 检查RAG服务健康状态
     * @return 是否健康
     */
    public boolean checkHealth() {
        try {
            String url = ragHttpUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("RAG服务健康检查失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 初始化RAG服务
     * @return 是否成功
     */
    public boolean initRag() {
        try {
            String url = ragHttpUrl + "/rag/init";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> requestEntity = new HttpEntity<>("{}", headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Boolean success = (Boolean) responseBody.get("success");
                if (success != null && success) {
                    log.info("RAG服务初始化成功");
                    return true;
                } else {
                    String error = (String) responseBody.get("error");
                    log.error("RAG服务初始化失败: {}", error);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("RAG服务初始化异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 创建错误结果
     */
    private RagAnswerVO createErrorResult(String errorMessage) {
        RagAnswerVO result = new RagAnswerVO();
        result.setAnswer("抱歉，RAG服务暂时不可用。错误信息: " + errorMessage);
        result.setIsCoralRelated(false);
        result.setSources(null);
        return result;
    }
}
