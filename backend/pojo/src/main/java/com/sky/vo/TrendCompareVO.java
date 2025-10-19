package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.List;

/**
 * Trend Data Comparison Response VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendCompareVO {
    
    /**
     * Indicator Name
     */
    private String indicator;
    
    /**
     * Indicator Data for Each Island
     * key: Island name, value: List of indicator values for all years
     */
    private Map<String, List<Double>> islandData;
    
    /**
     * Date Data for Each Island
     * key: Island name, value: List of dates for all years
     */
    private Map<String, List<String>> islandDates;
}
