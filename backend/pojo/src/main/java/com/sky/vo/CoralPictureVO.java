package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 珊瑚图片VO类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoralPictureVO {
    private String pictureUrl;  // 签名URL
    private String answer;      // 答案/分类
}
