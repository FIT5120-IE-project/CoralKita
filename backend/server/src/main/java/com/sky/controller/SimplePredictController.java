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
 * Simplified Coral Classification Prediction Controller
 */
@RestController
@RequestMapping("/predict")
@Slf4j
@Api(tags = "Coral Classification Prediction APIs")
public class SimplePredictController {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonApiUrl = "http://localhost:5000";
    
    /**
     * Upload image for coral classification prediction
     * @param imageFile Image file
     * @return Prediction result
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload image for coral classification prediction")
    public Result<Map<String, Object>> predictCoral(
            @ApiParam(value = "Coral image file", required = true) 
            @RequestPart("image") MultipartFile imageFile) {
        log.info("Received image prediction request, filename: {}, size: {} bytes", 
                imageFile.getOriginalFilename(), imageFile.getSize());
        
        try {
            // Validate file
            if (imageFile.isEmpty()) {
                return Result.error("Image file cannot be empty");
            }
            
            if (imageFile.getSize() > 16 * 1024 * 1024) {
                return Result.error("Image file size cannot exceed 16MB");
            }
            
            // Call Python API
            String predictUrl = pythonApiUrl + "/predict";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", imageFile.getResource());
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            try {
                @SuppressWarnings("rawtypes")
                ResponseEntity<Map> response = restTemplate.postForEntity(predictUrl, requestEntity, Map.class);
                
                if (response.getStatusCode() != HttpStatus.OK) {
                    return Result.error("Prediction service temporarily unavailable");
                }
                
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = response.getBody();
                if (responseBody == null || !Boolean.TRUE.equals(responseBody.get("success"))) {
                    String errorMessage = responseBody != null ? (String) responseBody.get("message") : "Prediction failed";
                    return Result.error(errorMessage);
                }
                
                // Parse result
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                
                String predictedClass = (String) data.get("predicted_class");
                Double confidence = (Double) data.get("confidence");
                @SuppressWarnings("unchecked")
                Map<String, Double> probabilities = (Map<String, Double>) data.get("probabilities");
                
                // Build return result
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("predicted_class", predictedClass);
                if (confidence != null) {
                    result.put("confidence", confidence);
                }
                if (probabilities != null) {
                    result.put("probabilities", probabilities);
                }
                
                log.info("Prediction completed, result: {} (confidence: {})", predictedClass, confidence);
                return Result.success(result);
                
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                // Handle 400 error (image quality does not meet requirements)
                if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    try {
                        // Parse error response
                        String responseBody = e.getResponseBodyAsString();
                        log.warn("Image quality validation failed: {}", responseBody);
                        
                        // Try to parse JSON error message
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        @SuppressWarnings("unchecked")
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
                                errorMessage += ". Suggestions: " + String.join(", ", suggestions);
                            }
                            
                            return Result.error(errorMessage);
                        }
                    } catch (Exception parseException) {
                        log.error("Failed to parse error response: {}", parseException.getMessage());
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
