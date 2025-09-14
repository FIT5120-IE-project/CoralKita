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
 * 趋势数据管理接口
 */
@RestController
@RequestMapping("/trend")
@Slf4j
@Api(tags = "趋势数据相关接口")
public class TrendController {

    @Autowired
    private TrendService trendService;


    /**
     * 根据岛屿名称查询趋势数据（GET方式）
     * @param island 岛屿名称
     * @return 趋势数据列表
     */
    @GetMapping("/query")
    @ApiOperation(value = "根据岛屿查询趋势数据")
    public Result<List<TrendVO>> getTrendDataByIslandGet(@RequestParam String island) {
        log.info("查询趋势数据：{}", island);
        
        if (island == null || island.trim().isEmpty()) {
            return Result.error("岛屿名称不能为空");
        }
        
        List<TrendVO> trendVOList = trendService.getTrendDataByIsland(island);
        
        
        // 检查是否找到数据
        if (trendVOList.isEmpty()) {
            return Result.error("无匹配岛屿");
        }

        return Result.success(trendVOList);
    }

    /**
     * 比较多个岛屿的指定指标数据
     * @param trendCompareDTO 比较参数
     * @return 比较结果列表
     */
    @PostMapping("/compare")
    @ApiOperation(value = "比较多个岛屿的指定指标数据")
    public Result<List<TrendCompareVO>> compareTrendData(@RequestBody TrendCompareDTO trendCompareDTO) {
        log.info("比较趋势数据：{}", trendCompareDTO);
        
        // 参数验证
        if (trendCompareDTO.getIslands() == null || trendCompareDTO.getIslands().isEmpty()) {
            return Result.error("岛屿列表不能为空");
        }
        // 执行比较查询
        List<TrendCompareVO> compareResult = trendService.compareTrendData(
                trendCompareDTO.getIslands()
        );
        // 检查是否找到数据
        if (compareResult.isEmpty()) {
            return Result.error("无匹配的岛屿数据");
        }
        return Result.success(compareResult);
    }

    /**
     * 获取所有岛屿列表
     * @return 岛屿列表
     */
    @GetMapping("/islands")
    @ApiOperation(value = "获取所有岛屿列表")
    public Result<List<String>> getAllIslands() {
        log.info("获取所有岛屿列表");
        
        List<String> islands = trendService.getAllIslands();
        
        if (islands.isEmpty()) {
            return Result.error("暂无岛屿数据");
        }

        return Result.success(islands);
    }

    /**
     * 根据岛屿名称查询珊瑚数据（bleach接口）
     * @param island 岛屿名称
     * @return 珊瑚数据列表
     */
    @GetMapping("/bleach")
    @ApiOperation(value = "根据岛屿查询珊瑚所有生数据(随前端需要使用：可供分析白化趋势,岛屿恢复速度，健康分析等等)")
    public Result<List<Coral>> getCoralDataByIsland(@RequestParam String island) {
        log.info("查询珊瑚数据：{}", island);
        
        if (island == null || island.trim().isEmpty()) {
            return Result.error("岛屿名称不能为空");
        }
        
        List<Coral> coralList = trendService.getCoralDataByIsland(island);
        
        // 检查是否找到数据
        if (coralList.isEmpty()) {
            return Result.error("无匹配岛屿的珊瑚数据");
        }

        return Result.success(coralList);
    }

    /**
     * 批量获取多个岛屿的坐标信息
     * @param islands 岛屿名称列表
     * @return 岛屿坐标信息列表
     */
    @PostMapping("/bleach/coordinates")
    @ApiOperation(value = "批量获取多个岛屿的坐标信息")
    public Result<Map<String, Object>> getIslandsCoordinates(@RequestBody List<String> islands) {
        log.info("批量获取岛屿坐标：{}", islands);
        
        if (islands == null || islands.isEmpty()) {
            return Result.error("岛屿列表不能为空");
        }
        
        Map<String, Object> coordinatesMap = trendService.getIslandsCoordinates(islands);
        
        return Result.success(coordinatesMap);
    }

    /**
     * 查询每个岛屿最新年份的趋势分析元数据
     * @return 趋势分析元数据列表
     */
    @GetMapping("/metadata/latest")
    @ApiOperation(value = "查询每个岛屿最新年份的趋势分析元数据")
    public Result<List<TrendAnalysisMetadata>> getLatestYearAllIslandsMetadata() {
        log.info("查询每个岛屿最新年份的趋势分析元数据");
        
        List<TrendAnalysisMetadata> metadataList = trendService.getLatestYearAllIslandsMetadata();
        
        // 检查是否找到数据
        if (metadataList.isEmpty()) {
            return Result.error("暂无趋势分析元数据");
        }

        return Result.success(metadataList);
    }

}
