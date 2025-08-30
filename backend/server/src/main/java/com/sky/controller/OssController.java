package com.sky.controller;

import com.sky.result.Result;
import com.sky.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oss")
@Slf4j
@Api(tags = "OSS文件管理接口")
public class OssController {

    @Autowired
    private OssService ossService;
    /**
     * 生成文件签名URL
     * @param objectKey 文件路径
     * @param expireSeconds 过期时间（秒）
     * @return 签名URL
     */
    @GetMapping("/url")
    @ApiOperation(value = "生成文件签名URL")
    public Result<String> generateSignedUrl(
            @RequestParam String objectKey,
            @RequestParam(defaultValue = "3600") int expireSeconds) {
        log.info("生成签名URL：{}，过期时间：{}秒", objectKey, expireSeconds);
        
        try {
            String signedUrl = ossService.generateSignedUrl(objectKey, expireSeconds);
            return Result.success(signedUrl);
        } catch (Exception e) {
            log.error("生成签名URL失败：{}", e.getMessage());
            return Result.error("生成失败：" + e.getMessage());
        }
    }

 

 
}
