package com.sky.service.impl;

import com.sky.entity.Trend;
import com.sky.entity.Coral;
import com.sky.mapper.TrendMapper;
import com.sky.service.TrendService;
import com.sky.vo.TrendVO;
import com.sky.vo.TrendCompareVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * 趋势数据服务实现类
 */
@Service
@Slf4j
public class TrendServiceImpl implements TrendService {

    @Autowired
    private TrendMapper trendMapper;

    /**
     * 根据岛屿名称查询趋势数据
     * @param island 岛屿名称
     * @return 趋势数据列表
     */
    @Override
    public List<TrendVO> getTrendDataByIsland(String island) {
        log.info("查询岛屿趋势数据：{}", island);
        
        // 查询数据库
        List<Trend> trendList = trendMapper.getByIsland(island);
        
        // 转换为VO对象
        List<TrendVO> trendVOList = trendList.stream().map(trend -> {
            return TrendVO.builder()
                    .date(trend.getDate())
                    .LCC(trend.getLcc())
                    .OT(trend.getOt())
                    .AS(trend.getAs())
                    .SD(trend.getSd())
                    .DI(trend.getDi())
                    .PI(trend.getPi())
                    .build();
        }).collect(Collectors.toList());
        
        log.info("查询到{}条趋势数据", trendVOList.size());
        return trendVOList;
    }

    /**
     * 比较多个岛屿的所有指标数据
     * @param islands 岛屿名称列表
     * @return 比较结果列表
     */
    @Override
    public List<TrendCompareVO> compareTrendData(List<String> islands) {
        log.info("比较岛屿趋势数据：{}", islands);
        
        // 查询所有岛屿的数据
        List<Trend> trendList = trendMapper.getByIslands(islands);
        
        // 按岛屿分组，然后按日期排序
        Map<String, List<Trend>> islandTrendMap = trendList.stream()
                .collect(Collectors.groupingBy(Trend::getIsland));
        
        // 定义所有指标
        String[] allIndicators = {"LCC", "OT", "AS", "SD", "DI", "PI"};
        
        // 为每个指标创建比较结果
        List<TrendCompareVO> compareVOList = new ArrayList<>();
        
        for (String indicator : allIndicators) {
            // 获取所有岛屿的所有年份数据
            Map<String, List<Double>> islandData = new HashMap<>();
            Map<String, List<String>> islandDates = new HashMap<>();
            
            for (String island : islands) {
                List<Trend> islandTrends = islandTrendMap.get(island);
                if (islandTrends != null && !islandTrends.isEmpty()) {
                    // 按日期降序排列
                    List<Trend> sortedTrends = islandTrends.stream()
                            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                            .collect(Collectors.toList());
                    
                    // 获取所有年份的指标数据
                    List<Double> values = sortedTrends.stream()
                            .map(trend -> getIndicatorValue(trend, indicator))
                            .filter(value -> value != null)
                            .collect(Collectors.toList());
                    
                    // 获取所有年份的日期数据
                    List<String> dates = sortedTrends.stream()
                            .map(trend -> trend.getDate().toString())
                            .collect(Collectors.toList());
                    
                    if (!values.isEmpty()) {
                        islandData.put(island, values);
                        islandDates.put(island, dates);
                    }
                }
            }
            
            // 转换为VO对象
            if (!islandData.isEmpty()) {
                compareVOList.add(TrendCompareVO.builder()
                        .indicator(indicator)
                        .islandData(islandData)
                        .islandDates(islandDates)
                        .build());
            }
        }
        
        log.info("比较结果：{}个指标，{}个岛屿", allIndicators.length, islands.size());
        return compareVOList;
    }
    
    /**
     * 根据指标名称获取对应的值
     * @param trend 趋势数据
     * @param indicator 指标名称
     * @return 指标值
     */
    private Double getIndicatorValue(Trend trend, String indicator) {
        switch (indicator.toUpperCase()) {
            case "LCC":
                return trend.getLcc();
            case "OT":
                return trend.getOt();
            case "AS":
                return trend.getAs();
            case "SD":
                return trend.getSd();
            case "DI":
                return trend.getDi();
            case "PI":
                return trend.getPi();
            default:
                log.warn("未知的指标类型：{}", indicator);
                return null;
        }
    }

    /**
     * 根据岛屿名称查询珊瑚数据
     * @param island 岛屿名称
     * @return 珊瑚数据列表
     */
    @Override
    public List<Coral> getCoralDataByIsland(String island) {
        log.info("查询岛屿珊瑚数据：{}", island);
        
        // 查询数据库
        List<Coral> coralList = trendMapper.getCoralByIslandOrderByDate(island);
        
        log.info("查询到{}条珊瑚数据", coralList.size());
        return coralList;
    }
}
