package com.sky.controller;

import com.sky.dto.TrendQueryDTO;
import com.sky.dto.TrendCompareDTO;
import com.sky.entity.Coral;
import com.sky.result.Result;
import com.sky.service.TrendService;
import com.sky.vo.TrendVO;
import com.sky.vo.TrendCompareVO;
import java.util.ArrayList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/query/{island}")
    @ApiOperation(value = "根据岛屿查询趋势数据")
    public Result<List<TrendVO>> getTrendDataByIslandGet(@PathVariable String island) {
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
     * 根据岛屿名称查询珊瑚数据（bleach接口）
     * @param island 岛屿名称
     * @return 珊瑚数据列表
     */
    @GetMapping("/bleach/{island}")
    @ApiOperation(value = "根据岛屿查询珊瑚所有生数据(随前端需要使用：可供分析白化趋势,岛屿恢复速度，健康分析等等)")
    public Result<List<Coral>> getCoralDataByIsland(@PathVariable String island) {
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

}
