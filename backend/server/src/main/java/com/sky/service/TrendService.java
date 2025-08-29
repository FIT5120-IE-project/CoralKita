package com.sky.service;

import com.sky.vo.TrendVO;
import com.sky.vo.TrendCompareVO;

import java.util.List;

/**
 * 趋势数据服务接口
 */
public interface TrendService {
    /**
     * 根据岛屿名称查询趋势数据
     * @param island 岛屿名称
     * @return 趋势数据列表
     */
    List<TrendVO> getTrendDataByIsland(String island);

    /**
     * 比较多个岛屿的所有指标数据
     * @param islands 岛屿名称列表
     * @return 比较结果列表
     */
    List<TrendCompareVO> compareTrendData(List<String> islands);
}
