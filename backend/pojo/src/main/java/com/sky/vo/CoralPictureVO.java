package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Coral Picture VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoralPictureVO {
    private String pictureUrl;  // Signed URL
    private String answer;      // Answer/Classification
}
