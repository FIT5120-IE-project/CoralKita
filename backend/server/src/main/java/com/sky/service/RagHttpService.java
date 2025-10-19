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
 * RAG HTTP Service Client
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
     * Call RAG HTTP service for Q&A
     * @param queryDTO Query request
     * @return RAG answer result
     */
    public RagAnswerVO queryRag(RagQueryDTO queryDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("Starting to call RAG HTTP service, question: {}", queryDTO.getQuestion());
            
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("question", queryDTO.getQuestion());
            requestBody.put("top_k", queryDTO.getTopK());
            requestBody.put("include_sources", queryDTO.getIncludeSources());
            
            // Set request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // Send request
            String url = ragHttpUrl + "/rag/query";
            log.info("Sending request to: {}", url);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                
                // Check if response is successful
                Boolean success = (Boolean) responseBody.get("success");
                if (success != null && success) {
                    // Build return result
                    RagAnswerVO result = new RagAnswerVO();
                    result.setAnswer((String) responseBody.get("answer"));
                    result.setIsCoralRelated((Boolean) responseBody.get("is_coral_related"));
                    
                    // Process source information
                    if (queryDTO.getIncludeSources() && responseBody.get("sources") != null) {
                        // TODO: Convert sources to List<SourceInfo>, temporarily set to null
                        result.setSources(null);
                    }
                    
                    long endTime = System.currentTimeMillis();
                    log.info("RAG HTTP service call successful, time: {}ms", endTime - startTime);
                    
                    return result;
                } else {
                    String error = (String) responseBody.get("error");
                    log.error("RAG HTTP service returned error: {}", error);
                    return createErrorResult("RAG service error: " + error);
                }
            } else {
                log.error("RAG HTTP service response abnormal, status code: {}", response.getStatusCode());
                return createErrorResult("RAG service response abnormal");
            }
            
        } catch (RestClientException e) {
            log.error("Failed to call RAG HTTP service: {}", e.getMessage(), e);
            return createErrorResult("Cannot connect to RAG service: " + e.getMessage());
        } catch (Exception e) {
            log.error("RAG HTTP service call exception: {}", e.getMessage(), e);
            return createErrorResult("RAG service call exception: " + e.getMessage());
        }
    }

    /**
     * Check RAG service health status
     * @return Whether healthy
     */
    public boolean checkHealth() {
        try {
            String url = ragHttpUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("RAG service health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Initialize RAG service
     * @return Whether successful
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
                    log.info("RAG service initialization successful");
                    return true;
                } else {
                    String error = (String) responseBody.get("error");
                    log.error("RAG service initialization failed: {}", error);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("RAG service initialization exception: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Create error result
     */
    private RagAnswerVO createErrorResult(String errorMessage) {
        RagAnswerVO result = new RagAnswerVO();
        result.setAnswer("Sorry, RAG service is temporarily unavailable. Error: " + errorMessage);
        result.setIsCoralRelated(false);
        result.setSources(null);
        return result;
    }
}
