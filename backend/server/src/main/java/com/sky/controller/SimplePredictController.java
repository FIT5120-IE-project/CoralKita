package com.sky.controller;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 简化的珊瑚分类预测控制器
 */
@RestController
@RequestMapping("/predict")
@Slf4j
@Api(tags = "珊瑚分类预测接口")
public class SimplePredictController {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonApiUrl = "http://localhost:5000";
    
    /**
     * 上传图片进行珊瑚分类预测
     * @param imageFile 图片文件
     * @return 预测结果
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传图片进行珊瑚分类预测")
    public Result<String> predictCoral(
            @ApiParam(value = "珊瑚图片文件", required = true) 
            @RequestPart("image") MultipartFile imageFile) {
        log.info("收到图片预测请求，文件名: {}, 大小: {} bytes", 
                imageFile.getOriginalFilename(), imageFile.getSize());
        
        try {
            // 验证文件
            if (imageFile.isEmpty()) {
                return Result.error("Image file cannot be empty");
            }
            
            if (imageFile.getSize() > 16 * 1024 * 1024) {
                return Result.error("Image file size cannot exceed 16MB");
            }
            
            // 调用Python API
            String predictUrl = pythonApiUrl + "/predict";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", imageFile.getResource());
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(predictUrl, requestEntity, Map.class);
                
                if (response.getStatusCode() != HttpStatus.OK) {
                    return Result.error("Prediction service temporarily unavailable");
                }
                
                Map<String, Object> responseBody = response.getBody();
                if (responseBody == null || !Boolean.TRUE.equals(responseBody.get("success"))) {
                    String errorMessage = responseBody != null ? (String) responseBody.get("message") : "Prediction failed";
                    return Result.error(errorMessage);
                }
                
                // 解析结果
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                
                String predictedClass = (String) data.get("predicted_class");
                
                log.info("预测完成，结果: {}", predictedClass);
                return Result.success(predictedClass);
                
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                // 处理400错误（图片质量不符合要求）
                if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    try {
                        // 解析错误响应
                        String responseBody = e.getResponseBodyAsString();
                        log.warn("图片质量验证失败: {}", responseBody);
                        
                        // 尝试解析JSON错误信息
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        Map<String, Object> errorResponse = mapper.readValue(responseBody, Map.class);
                        
                        @SuppressWarnings("unchecked")
                        Map<String, Object> data = (Map<String, Object>) errorResponse.get("data");
                        
                        if (data != null) {
                            @SuppressWarnings("unchecked")
                            java.util.List<String> issues = (java.util.List<String>) data.get("issues");
                            @SuppressWarnings("unchecked")
                            java.util.List<String> suggestions = (java.util.List<String>) data.get("suggestions");
                            
                            String errorMessage = "Image quality does not meet the requirements: " + String.join(", ", issues);
                            if (suggestions != null && !suggestions.isEmpty()) {
                                errorMessage += "。suggestions: " + String.join(", ", suggestions);
                            }
                            
                            return Result.error(errorMessage);
                        }
                    } catch (Exception parseException) {
                        log.error("解析错误响应失败: {}", parseException.getMessage());
                    }
                    
                    return Result.error("Image quality does not meet requirements, please upload a clear coral image");
                }
                throw e;
            }
            
        } catch (Exception e) {
            log.error("Prediction failed: {}", e.getMessage(), e);
            return Result.error("Prediction failed: " + e.getMessage());
        }
    }
}
