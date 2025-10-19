package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Coral Picture Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoralPicture {
    private Long id;
    private String picture;  // Picture URL
    private String answer;   // Answer/Classification
}
