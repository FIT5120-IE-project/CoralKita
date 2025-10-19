package com.sky.controller;

import com.sky.entity.CoralPicture;
import com.sky.mapper.CoralPictureMapper;
import com.sky.result.Result;
import com.sky.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oss")
@Slf4j
@Api(tags = "OSS File Management APIs")
public class OssController {

    @Autowired
    private OssService ossService;

    @Autowired
    private CoralPictureMapper coralPictureMapper;
    /**
     * Generate signed URL for file
     * @param objectKey File path
     * @param expireSeconds Expiration time (seconds)
     * @return Signed URL
     */
    @GetMapping("/url")
    @ApiOperation(value = "Generate signed URL for file")
    public Result<String> generateSignedUrl(
            @RequestParam String objectKey,
            @RequestParam(defaultValue = "3600") int expireSeconds) {
        log.info("Generating signed URL: {}, expiration time: {} seconds", objectKey, expireSeconds);
        
        try {
            String signedUrl = ossService.generateSignedUrl(objectKey, expireSeconds);
            return Result.success(signedUrl);
        } catch (Exception e) {
            log.error("Failed to generate signed URL: {}", e.getMessage());
            return Result.error("Generation failed: " + e.getMessage());
        }
    }

    /**
     * Batch upload all images in folder to OSS and save to database
     * @param folderPath Folder path
     * @param answer Answer/classification
     * @return Upload result
     */
    @PostMapping("/upload-folder")
    @ApiOperation(value = "Batch upload all images in folder")
    public Result<String> uploadFolderImages(
            @RequestParam String folderPath,
            @RequestParam String answer) {
        log.info("Batch uploading folder images: {}, answer: {}", folderPath, answer);
        
        try {
            // 1. Upload all images in folder to OSS
            List<String> uploadedUrls = ossService.uploadFolderImages(folderPath, answer);
            
            // 2. Save image URLs to database
            for (String url : uploadedUrls) {
                CoralPicture coralPicture = CoralPicture.builder()
                        .picture(url)
                        .answer(answer)
                        .build();
                coralPictureMapper.insert(coralPicture);
            }
            
            String result = String.format("Successfully uploaded %d images to OSS image/%s directory and saved to database", 
                    uploadedUrls.size(), answer);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("Failed to batch upload images: {}", e.getMessage());
            return Result.error("Upload failed: " + e.getMessage());
        }
    }

    /**
     * Get signed URL for single video file
     * @param videoFileName Video file name
     * @param expireSeconds Expiration time (seconds), default 3600 seconds (1 hour)
     * @return Video signed URL
     */
    @GetMapping("/video/url")
    @ApiOperation(value = "Get signed URL for single video file")
    public Result<String> getVideoSignedUrl(
            @RequestParam String videoFileName,
            @RequestParam(defaultValue = "3600") int expireSeconds) {
        log.info("Getting video signed URL: {}, expiration time: {} seconds", videoFileName, expireSeconds);
        
        try {
            String signedUrl = ossService.getVideoSignedUrl(videoFileName, expireSeconds);
            return Result.success(signedUrl);
        } catch (Exception e) {
            log.error("Failed to get video signed URL: {}", e.getMessage());
            return Result.error("Failed to get: " + e.getMessage());
        }
    }


}
