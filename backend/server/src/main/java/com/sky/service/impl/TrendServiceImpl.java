package com.sky.service.impl;

import com.sky.entity.Trend;
import com.sky.entity.Coral;
import com.sky.entity.TrendAnalysisMetadata;
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

/**
 * Trend Data Service Implementation
 */
@Service
@Slf4j
public class TrendServiceImpl implements TrendService {

    @Autowired
    private TrendMapper trendMapper;

    /**
     * Query trend data by island name
     * @param island Island name
     * @return List of trend data
     */
    @Override
    public List<TrendVO> getTrendDataByIsland(String island) {
        log.info("Querying island trend data: {}", island);
        
        // Query database
        List<Trend> trendList = trendMapper.getByIsland(island);
        
        // Convert to VO objects
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
        
        log.info("Found {} trend data records", trendVOList.size());
        return trendVOList;
    }

    /**
     * Compare all indicator data of multiple islands
     * @param islands List of island names
     * @return List of comparison results
     */
    @Override
    public List<TrendCompareVO> compareTrendData(List<String> islands) {
        log.info("Comparing island trend data: {}", islands);
        
        // Query data for all islands
        List<Trend> trendList = trendMapper.getByIslands(islands);
        
        // Group by island, then sort by date
        Map<String, List<Trend>> islandTrendMap = trendList.stream()
                .collect(Collectors.groupingBy(Trend::getIsland));
        
        // Define all indicators
        String[] allIndicators = {"LCC", "OT", "AS", "SD", "DI", "PI"};
        
        // Create comparison results for each indicator
        List<TrendCompareVO> compareVOList = new ArrayList<>();
        
        for (String indicator : allIndicators) {
            // Get data for all years for all islands
            Map<String, List<Double>> islandData = new HashMap<>();
            Map<String, List<String>> islandDates = new HashMap<>();
            
            for (String island : islands) {
                List<Trend> islandTrends = islandTrendMap.get(island);
                if (islandTrends != null && !islandTrends.isEmpty()) {
                    // Sort by date in descending order
                    List<Trend> sortedTrends = islandTrends.stream()
                            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                            .collect(Collectors.toList());
                    
                    // Get indicator data for all years
                    List<Double> values = sortedTrends.stream()
                            .map(trend -> getIndicatorValue(trend, indicator))
                            .filter(value -> value != null)
                            .collect(Collectors.toList());
                    
                    // Get date data for all years
                    List<String> dates = sortedTrends.stream()
                            .map(trend -> trend.getDate().toString())
                            .collect(Collectors.toList());
                    
                    if (!values.isEmpty()) {
                        islandData.put(island, values);
                        islandDates.put(island, dates);
                    }
                }
            }
            
            // Convert to VO objects
            if (!islandData.isEmpty()) {
                compareVOList.add(TrendCompareVO.builder()
                        .indicator(indicator)
                        .islandData(islandData)
                        .islandDates(islandDates)
                        .build());
            }
        }
        
        log.info("Comparison result: {} indicators, {} islands", allIndicators.length, islands.size());
        return compareVOList;
    }
    
    /**
     * Get indicator value by indicator name
     * @param trend Trend data
     * @param indicator Indicator name
     * @return Indicator value
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
                log.warn("Unknown indicator type: {}", indicator);
                return null;
        }
    }

    /**
     * Query coral data by island name
     * @param island Island name
     * @return List of coral data
     */
    @Override
    public List<Coral> getCoralDataByIsland(String island) {
        log.info("Querying island coral data: {}", island);
        
        // Query database
        List<Coral> coralList = trendMapper.getCoralByIslandOrderByDate(island);
        
        log.info("Found {} coral data records", coralList.size());
        return coralList;
    }

    /**
     * Get all island list
     * @return Island list
     */
    @Override
    public List<String> getAllIslands() {
        log.info("Getting all island list");
        List<String> islands = trendMapper.getAllIslands();
        log.info("Found {} islands", islands.size());
        return islands;
    }

    /**
     * Batch get coordinates of multiple islands
     * @param islands List of island names
     * @return Map of island coordinates, where key is island name and value is coordinate information
     */
    @Override
    public Map<String, Object> getIslandsCoordinates(List<String> islands) {
        log.info("Batch getting island coordinates: {}", islands);
        
        Map<String, Object> coordinatesMap = new HashMap<>();
        
        try {
            // Use single SQL query to get coordinates for all islands
            List<Coral> coralList = trendMapper.getIslandsCoordinatesBatch(islands);
            log.info("Single SQL query found {} island coordinate data records", coralList.size());
            
            // Convert query results to Map format
            for (Coral coral : coralList) {
                Map<String, Object> islandInfo = new HashMap<>();
                islandInfo.put("lat", coral.getIslandLat());
                islandInfo.put("lng", coral.getIslandLng());
                islandInfo.put("hasData", true);
                coordinatesMap.put(coral.getIsland(), islandInfo);
            }
            
            // Add markers for islands without coordinate data
            for (String island : islands) {
                if (!coordinatesMap.containsKey(island)) {
                    Map<String, Object> islandInfo = new HashMap<>();
                    islandInfo.put("hasData", false);
                    coordinatesMap.put(island, islandInfo);
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to batch get island coordinates: {}", e.getMessage(), e);
            // Add error markers for all islands on error
            for (String island : islands) {
                Map<String, Object> islandInfo = new HashMap<>();
                islandInfo.put("hasData", false);
                islandInfo.put("error", e.getMessage());
                coordinatesMap.put(island, islandInfo);
            }
        }
        
        log.info("Successfully got coordinates for {} islands", coordinatesMap.size());
        return coordinatesMap;
    }

    /**
     * Query trend analysis metadata for latest year of each island
     * @return List of trend analysis metadata
     */
    @Override
    public List<TrendAnalysisMetadata> getLatestYearAllIslandsMetadata() {
        log.info("Querying trend analysis metadata for latest year of each island");
        
        // Query database
        List<TrendAnalysisMetadata> metadataList = trendMapper.getLatestYearAllIslandsMetadata();
        
        log.info("Found {} trend analysis metadata records for latest year of each island", metadataList.size());
        return metadataList;
    }

    /**
     * Batch get trend data for all islands
     * @return Map of all islands' trend data, where key is island name and value is list of trend data
     */
    @Override
    public Map<String, List<TrendVO>> getAllIslandsTrendData() {
        log.info("Batch getting trend data for all islands");
        
        Map<String, List<TrendVO>> allIslandsTrendData = new HashMap<>();
        
        try {
            // Get all island list
            List<String> allIslands = trendMapper.getAllIslands();
            log.info("Got {} islands", allIslands.size());
            
            // Batch query trend data for all islands
            List<Trend> allTrends = trendMapper.getByIslands(allIslands);
            log.info("Batch query found {} trend data records", allTrends.size());
            
            // Group by island and convert to VO
            Map<String, List<Trend>> trendsByIsland = allTrends.stream()
                    .collect(Collectors.groupingBy(Trend::getIsland));
            
            // Convert data for each island
            for (Map.Entry<String, List<Trend>> entry : trendsByIsland.entrySet()) {
                String island = entry.getKey();
                List<Trend> islandTrends = entry.getValue();
                
                List<TrendVO> trendVOList = islandTrends.stream().map(trend -> {
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
                
                allIslandsTrendData.put(island, trendVOList);
                log.info("Island {} has {} trend data records", island, trendVOList.size());
            }
            
            // Add empty lists for islands without data
            for (String island : allIslands) {
                if (!allIslandsTrendData.containsKey(island)) {
                    allIslandsTrendData.put(island, new ArrayList<>());
                    log.warn("Island {} has no trend data", island);
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to batch get trend data for all islands: {}", e.getMessage(), e);
            // Return empty Map instead of throwing exception
            return new HashMap<>();
        }
        
        log.info("Successfully got trend data for {} islands", allIslandsTrendData.size());
        return allIslandsTrendData;
    }

    /**
     * Batch get coral data for all islands (bleach data)
     * @return Map of all islands' coral data, where key is island name and value is list of coral data
     */
    @Override
    public Map<String, List<Coral>> getAllIslandsCoralData() {
        log.info("Batch getting coral data for all islands");
        
        Map<String, List<Coral>> allIslandsCoralData = new HashMap<>();
        
        try {
            // Batch query coral data for all islands
            List<Coral> allCorals = trendMapper.getAllIslandsCoralData();
            log.info("Batch query found {} coral data records", allCorals.size());
            
            // Group by island
            Map<String, List<Coral>> coralsByIsland = allCorals.stream()
                    .collect(Collectors.groupingBy(Coral::getIsland));
            
            // Process data for each island
            for (Map.Entry<String, List<Coral>> entry : coralsByIsland.entrySet()) {
                String island = entry.getKey();
                List<Coral> islandCorals = entry.getValue();
                
                allIslandsCoralData.put(island, islandCorals);
                log.info("Island {} has {} coral data records", island, islandCorals.size());
            }
            
        } catch (Exception e) {
            log.error("Failed to batch get coral data for all islands: {}", e.getMessage(), e);
            // Return empty Map instead of throwing exception
            return new HashMap<>();
        }
        
        log.info("Successfully got coral data for {} islands", allIslandsCoralData.size());
        return allIslandsCoralData;
    }
}
