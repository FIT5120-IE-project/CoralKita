package com.sky.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

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
}
