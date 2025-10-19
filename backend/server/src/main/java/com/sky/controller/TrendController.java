package com.sky.controller;

import com.sky.dto.TrendCompareDTO;
import com.sky.entity.Coral;
import com.sky.entity.TrendAnalysisMetadata;
import com.sky.result.Result;
import com.sky.service.TrendService;
import com.sky.vo.TrendVO;
import com.sky.vo.TrendCompareVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Trend Data Management Controller
 */
@RestController
@RequestMapping("/trend")
@Slf4j
@Api(tags = "Trend Data Related APIs")
public class TrendController {

    @Autowired
    private TrendService trendService;


    /**
     * Query trend data by island name (GET method)
     * @param island Island name
     * @return List of trend data
     */
    @GetMapping("/query")
    @ApiOperation(value = "Query trend data by island")
    public Result<List<TrendVO>> getTrendDataByIslandGet(@RequestParam String island) {
        log.info("Querying trend data: {}", island);
        
        if (island == null || island.trim().isEmpty()) {
            return Result.error("Island name cannot be empty");
        }
        
        List<TrendVO> trendVOList = trendService.getTrendDataByIsland(island);
        
        
        // Check if data is found
        if (trendVOList.isEmpty()) {
            return Result.error("No matching island");
        }

        return Result.success(trendVOList);
    }

    /**
     * Compare specified indicator data of multiple islands
     * @param trendCompareDTO Comparison parameters
     * @return List of comparison results
     */
    @PostMapping("/compare")
    @ApiOperation(value = "Compare specified indicator data of multiple islands")
    public Result<List<TrendCompareVO>> compareTrendData(@RequestBody TrendCompareDTO trendCompareDTO) {
        log.info("Comparing trend data: {}", trendCompareDTO);
        
        // Parameter validation
        if (trendCompareDTO.getIslands() == null || trendCompareDTO.getIslands().isEmpty()) {
            return Result.error("Island list cannot be empty");
        }
        // Execute comparison query
        List<TrendCompareVO> compareResult = trendService.compareTrendData(
                trendCompareDTO.getIslands()
        );
        // Check if data is found
        if (compareResult.isEmpty()) {
            return Result.error("No matching island data");
        }
        return Result.success(compareResult);
    }

    /**
     * Get all island list
     * @return Island list
     */
    @GetMapping("/islands")
    @ApiOperation(value = "Get all island list")
    public Result<List<String>> getAllIslands() {
        log.info("Getting all island list");
        
        List<String> islands = trendService.getAllIslands();
        
        if (islands.isEmpty()) {
            return Result.error("No island data available");
        }

        return Result.success(islands);
    }

    /**
     * Query coral data by island name (bleach endpoint)
     * @param island Island name
     * @return List of coral data
     */
    @GetMapping("/bleach")
    @ApiOperation(value = "Query all raw coral data by island (for frontend use: can be used to analyze bleaching trends, island recovery speed, health analysis, etc.)")
    public Result<List<Coral>> getCoralDataByIsland(@RequestParam String island) {
        log.info("Querying coral data: {}", island);
        
        if (island == null || island.trim().isEmpty()) {
            return Result.error("Island name cannot be empty");
        }
        
        List<Coral> coralList = trendService.getCoralDataByIsland(island);
        
        // Check if data is found
        if (coralList.isEmpty()) {
            return Result.error("No coral data for matching island");
        }

        return Result.success(coralList);
    }

    /**
     * Batch get coordinates of multiple islands
     * @param islands List of island names
     * @return List of island coordinates
     */
    @PostMapping("/bleach/coordinates")
    @ApiOperation(value = "Batch get coordinates of multiple islands")
    public Result<Map<String, Object>> getIslandsCoordinates(@RequestBody List<String> islands) {
        log.info("Batch getting island coordinates: {}", islands);
        
        if (islands == null || islands.isEmpty()) {
            return Result.error("Island list cannot be empty");
        }
        
        Map<String, Object> coordinatesMap = trendService.getIslandsCoordinates(islands);
        
        return Result.success(coordinatesMap);
    }

    /**
     * Query trend analysis metadata for latest year of each island
     * @return List of trend analysis metadata
     */
    @GetMapping("/metadata/latest")
    @ApiOperation(value = "Query trend analysis metadata for latest year of each island")
    public Result<List<TrendAnalysisMetadata>> getLatestYearAllIslandsMetadata() {
        log.info("Querying trend analysis metadata for latest year of each island");
        
        List<TrendAnalysisMetadata> metadataList = trendService.getLatestYearAllIslandsMetadata();
        
        // Check if data is found
        if (metadataList.isEmpty()) {
            return Result.error("No trend analysis metadata available");
        }

        return Result.success(metadataList);
    }

    /**
     * Batch get trend data for all islands
     * @return Map of all islands' trend data, where key is island name and value is list of trend data
     */
    @GetMapping("/query/all")
    @ApiOperation(value = "Batch get trend data for all islands")
    public Result<Map<String, List<TrendVO>>> getAllIslandsTrendData() {
        log.info("Batch getting trend data for all islands");
        
        Map<String, List<TrendVO>> allIslandsTrendData = trendService.getAllIslandsTrendData();
        
        // Check if data is found
        if (allIslandsTrendData.isEmpty()) {
            return Result.error("No trend data available");
        }

        return Result.success(allIslandsTrendData);
    }

    /**
     * Batch get coral data for all islands (bleach data)
     * @return Map of all islands' coral data, where key is island name and value is list of coral data
     */
    @GetMapping("/bleach/all")
    @ApiOperation(value = "Batch get coral data for all islands")
    public Result<Map<String, List<Coral>>> getAllIslandsCoralData() {
        log.info("Batch getting coral data for all islands");
        
        Map<String, List<Coral>> allIslandsCoralData = trendService.getAllIslandsCoralData();
        
        // Check if data is found
        if (allIslandsCoralData.isEmpty()) {
            return Result.error("No coral data available");
        }

        return Result.success(allIslandsCoralData);
    }

}
