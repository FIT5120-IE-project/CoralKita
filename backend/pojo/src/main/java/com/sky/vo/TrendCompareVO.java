package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.List;

/**
 * 趋势数据比较返回VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendCompareVO {
    
    /**
     * 各岛屿的指标数据
     * key: 岛屿名称, value: 所有年份的指标值列表
     */
    private Map<String, List<Double>> islandData;
    
    /**
     * 各岛屿的日期数据
     * key: 岛屿名称, value: 所有年份的日期列表
     */
    private Map<String, List<String>> islandDates;
}
