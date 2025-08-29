package com.sky.dto;

import lombok.Data;

import java.util.List;

/**
 * 趋势数据比较查询DTO
 */
@Data
public class TrendCompareDTO {
    /**
     * 岛屿名称列表
     */
    private List<String> islands;
}
