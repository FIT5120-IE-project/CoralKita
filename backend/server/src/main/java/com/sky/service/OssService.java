package com.sky.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OssService {

    private final OSS ossClient;

    @Value("${aliyun.oss.bucket}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    public OssService(OSS ossClient) {
        this.ossClient = ossClient;
    }

    /**
     * 生成签名 URL
     * @param objectKey 文件的 OSS 名称
     * @param expireSeconds 签名的有效期，单位秒
     * @return 签名 URL
     */
    public String generateSignedUrl(String objectKey, int expireSeconds) {
        Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000L);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey, HttpMethod.GET);
        request.setExpiration(expiration);
        
        // 获取签名 URL
        URL signedUrl = ossClient.generatePresignedUrl(request);
        return signedUrl.toString();
    }

    /**
     * 测试读取OSS文件
     * @param objectKey 文件路径
     * @return 文件是否存在
     */
    public boolean testFileExists(String objectKey) {
        try {
            return ossClient.doesObjectExist(bucketName, objectKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件信息
     * @param objectKey 文件路径
     * @return 文件信息
     */
    public String getFileInfo(String objectKey) {
        try {
            if (ossClient.doesObjectExist(bucketName, objectKey)) {
                long fileSize = ossClient.getObjectMetadata(bucketName, objectKey).getContentLength();
                return String.format("文件存在，大小: %d bytes", fileSize);
            } else {
                return "文件不存在";
            }
        } catch (Exception e) {
            return "获取文件信息失败: " + e.getMessage();
        }
    }

    /**
     * 将OSS文件流式输出到HTTP响应
     * @param objectKey 文件路径
     * @param response HTTP响应对象
     */
    public void streamFileToResponse(String objectKey, HttpServletResponse response) throws Exception {
        OSSObject ossObject = ossClient.getObject(bucketName, objectKey);
        
        try (InputStream inputStream = ossObject.getObjectContent();
             OutputStream outputStream = response.getOutputStream()) {
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } finally {
            if (ossObject != null) {
                ossObject.close();
            }
        }
    }

    /**
     * 上传单个文件到OSS
     * @param file 要上传的文件
     * @param objectKey OSS中的对象键
     * @return 上传后的URL
     */
    public String uploadFile(File file, String objectKey) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file);
            ossClient.putObject(putObjectRequest);
            
            // 返回文件的访问URL
            return "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectKey;
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 批量上传文件夹中的所有图片文件
     * @param folderPath 文件夹路径
     * @param answer 答案/分类
     * @return 上传结果列表
     */
    public List<String> uploadFolderImages(String folderPath, String answer) {
        List<String> uploadedUrls = new ArrayList<>();
        File folder = new File(folderPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("文件夹不存在: " + folderPath);
        }
        
        File[] files = folder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || 
                   lowerName.endsWith(".png") || lowerName.endsWith(".gif") || 
                   lowerName.endsWith(".webp");
        });
        
        if (files == null || files.length == 0) {
            throw new RuntimeException("文件夹中没有找到图片文件");
        }
        
        for (File file : files) {
            try {
                // 生成OSS对象键：image/health/文件名
                String objectKey = "image/" + answer + "/" + file.getName();
                String url = uploadFile(file, objectKey);
                uploadedUrls.add(url);
            } catch (Exception e) {
                throw new RuntimeException("上传文件 " + file.getName() + " 失败: " + e.getMessage(), e);
            }
        }
        
        return uploadedUrls;
    }

    /**
     * 获取视频文件的签名URL
     * @param videoFileName 视频文件名（不包含路径）
     * @param expireSeconds 过期时间（秒），默认1小时
     * @return 视频的签名URL
     */
    public String getVideoSignedUrl(String videoFileName, int expireSeconds) {
        // 构建完整的对象键：vedio/文件名
        String objectKey = "vedio/" + videoFileName;
        
        // 检查文件是否存在
        if (!ossClient.doesObjectExist(bucketName, objectKey)) {
            throw new RuntimeException("视频文件不存在: " + videoFileName);
        }
        
        return generateSignedUrl(objectKey, expireSeconds);
    }

}
