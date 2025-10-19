package com.sky.dto;

import lombok.Data;

import java.util.List;

/**
 * Trend Data Comparison Query DTO
 */
@Data
public class TrendCompareDTO {
    /**
     * List of Island Names
     */
    private List<String> islands;
}
