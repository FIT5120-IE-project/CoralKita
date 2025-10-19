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
     * Generate signed URL
     * @param objectKey OSS object key
     * @param expireSeconds Expiration time in seconds
     * @return Signed URL
     */
    public String generateSignedUrl(String objectKey, int expireSeconds) {
        Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000L);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey, HttpMethod.GET);
        request.setExpiration(expiration);
        
        // Get signed URL
        URL signedUrl = ossClient.generatePresignedUrl(request);
        return signedUrl.toString();
    }

    /**
     * Test if OSS file exists
     * @param objectKey File path
     * @return Whether file exists
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
     * Get file information
     * @param objectKey File path
     * @return File information
     */
    public String getFileInfo(String objectKey) {
        try {
            if (ossClient.doesObjectExist(bucketName, objectKey)) {
                long fileSize = ossClient.getObjectMetadata(bucketName, objectKey).getContentLength();
                return String.format("File exists, size: %d bytes", fileSize);
            } else {
                return "File does not exist";
            }
        } catch (Exception e) {
            return "Failed to get file info: " + e.getMessage();
        }
    }

    /**
     * Stream OSS file to HTTP response
     * @param objectKey File path
     * @param response HTTP response object
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
     * Upload single file to OSS
     * @param file File to upload
     * @param objectKey OSS object key
     * @return Uploaded file URL
     */
    public String uploadFile(File file, String objectKey) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file);
            ossClient.putObject(putObjectRequest);
            
            // Return file access URL
            return "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectKey;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    /**
     * Batch upload all image files in folder
     * @param folderPath Folder path
     * @param answer Answer/Classification
     * @return List of uploaded URLs
     */
    public List<String> uploadFolderImages(String folderPath, String answer) {
        List<String> uploadedUrls = new ArrayList<>();
        File folder = new File(folderPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Folder does not exist: " + folderPath);
        }
        
        File[] files = folder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || 
                   lowerName.endsWith(".png") || lowerName.endsWith(".gif") || 
                   lowerName.endsWith(".webp");
        });
        
        if (files == null || files.length == 0) {
            throw new RuntimeException("No image files found in folder");
        }
        
        for (File file : files) {
            try {
                // Generate OSS object key: image/health/filename
                String objectKey = "image/" + answer + "/" + file.getName();
                String url = uploadFile(file, objectKey);
                uploadedUrls.add(url);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file " + file.getName() + ": " + e.getMessage(), e);
            }
        }
        
        return uploadedUrls;
    }

    /**
     * Get signed URL for video file
     * @param videoFileName Video file name (without path)
     * @param expireSeconds Expiration time in seconds, default 1 hour
     * @return Signed URL for video
     */
    public String getVideoSignedUrl(String videoFileName, int expireSeconds) {
        // Build complete object key: vedio/filename
        String objectKey = "vedio/" + videoFileName;
        
        // Check if file exists
        if (!ossClient.doesObjectExist(bucketName, objectKey)) {
            throw new RuntimeException("Video file does not exist: " + videoFileName);
        }
        
        return generateSignedUrl(objectKey, expireSeconds);
    }

}
