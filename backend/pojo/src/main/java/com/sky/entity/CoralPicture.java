package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 珊瑚图片实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoralPicture {
    private Long id;
    private String picture;  // 图片URL
    private String answer;   // 答案/分类
}
