package com.sky.service;

import com.sky.entity.Coral;
import com.sky.entity.TrendAnalysisMetadata;
import com.sky.vo.TrendVO;
import com.sky.vo.TrendCompareVO;

import java.util.List;
import java.util.Map;

/**
 * Trend Data Service Interface
 */
public interface TrendService {
    /**
     * Query trend data by island name
     * @param island Island name
     * @return List of trend data
     */
    List<TrendVO> getTrendDataByIsland(String island);

    /**
     * Compare specified indicator data of multiple islands
     * @param islands List of island names
     * @return List of comparison results
     */
    List<TrendCompareVO> compareTrendData(List<String> islands);

    /**
     * Get all island list
     * @return Island list
     */
    List<String> getAllIslands();

    /**
     * Query coral data by island name
     * @param island Island name
     * @return List of coral data
     */
    List<Coral> getCoralDataByIsland(String island);

    /**
     * Batch get trend data for all islands
     * @return Map of all islands' trend data, where key is island name and value is list of trend data
     */
    Map<String, List<TrendVO>> getAllIslandsTrendData();

    /**
     * Batch get coral data for all islands (bleach data)
     * @return Map of all islands' coral data, where key is island name and value is list of coral data
     */
    Map<String, List<Coral>> getAllIslandsCoralData();

    /**
     * Batch get coordinates of multiple islands
     * @param islands List of island names
     * @return Map of island coordinates, where key is island name and value is coordinate information
     */
    Map<String, Object> getIslandsCoordinates(List<String> islands);

    /**
     * Query trend analysis metadata for latest year of each island
     * @return List of trend analysis metadata
     */
    List<TrendAnalysisMetadata> getLatestYearAllIslandsMetadata();
}
