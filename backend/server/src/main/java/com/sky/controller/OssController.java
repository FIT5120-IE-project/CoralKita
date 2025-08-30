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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/oss")
@Slf4j
@Api(tags = "OSS文件管理接口")
public class OssController {

    @Autowired
    private OssService ossService;

    @Autowired
    private CoralPictureMapper coralPictureMapper;
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

    /**
     * 批量上传文件夹中的所有图片到OSS并存入数据库
     * @param folderPath 文件夹路径
     * @param answer 答案/分类
     * @return 上传结果
     */
    @PostMapping("/upload-folder")
    @ApiOperation(value = "批量上传文件夹中的所有图片")
    public Result<String> uploadFolderImages(
            @RequestParam String folderPath,
            @RequestParam String answer) {
        log.info("批量上传文件夹图片：{}，答案：{}", folderPath, answer);
        
        try {
            // 1. 上传文件夹中的所有图片到OSS
            List<String> uploadedUrls = ossService.uploadFolderImages(folderPath, answer);
            
            // 2. 将图片URL存入数据库
            for (String url : uploadedUrls) {
                CoralPicture coralPicture = CoralPicture.builder()
                        .picture(url)
                        .answer(answer)
                        .build();
                coralPictureMapper.insert(coralPicture);
            }
            
            String result = String.format("成功上传 %d 张图片到OSS的 image/%s 目录，并存入数据库", 
                    uploadedUrls.size(), answer);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量上传图片失败：{}", e.getMessage());
            return Result.error("上传失败：" + e.getMessage());
        }
    }

}
