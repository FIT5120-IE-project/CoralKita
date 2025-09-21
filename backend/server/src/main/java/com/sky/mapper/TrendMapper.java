package com.sky.mapper;

import com.sky.entity.Trend;
import com.sky.entity.Coral;
import com.sky.entity.TrendAnalysisMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrendMapper {

    /**
     * 根据岛屿名称查询所有趋势数据
     * @param island 岛屿名称
     * @return 趋势数据列表
     */
    @Select("select * from trend where island = #{island} order by date desc")
    List<Trend> getByIsland(String island);

    /**
     * 根据多个岛屿名称查询趋势数据
     * @param islands 岛屿名称列表
     * @return 趋势数据列表
     */
    @Select("<script>" +
            "select * from trend where island in " +
            "<foreach collection='islands' item='island' open='(' separator=',' close=')'>" +
            "#{island}" +
            "</foreach>" +
            " order by date desc" +
            "</script>")
    List<Trend> getByIslands(List<String> islands);

    /**
     * 获取所有岛屿列表
     * @return 岛屿列表
     */
    @Select("select distinct island from trend order by island")
    List<String> getAllIslands();

    /**
     * 根据岛屿名称查询珊瑚数据，按日期排序
     * @param island 岛屿名称
     * @return 珊瑚数据列表
     */
    @Select("select * from coral where island = #{island} order by date desc")
    List<Coral> getCoralByIslandOrderByDate(String island);

    /**
     * 批量获取多个岛屿的坐标信息（每个岛屿取最新的一条记录）
     * @param islands 岛屿名称列表
     * @return 珊瑚数据列表，包含每个岛屿的最新坐标
     */
    @Select("<script>" +
            "SELECT c1.* FROM coral c1 " +
            "INNER JOIN (" +
            "  SELECT island, MAX(date) as max_date " +
            "  FROM coral " +
            "  WHERE island IN " +
            "  <foreach collection='islands' item='island' open='(' separator=',' close=')'>" +
            "    #{island}" +
            "  </foreach>" +
            "  GROUP BY island" +
            ") c2 ON c1.island = c2.island AND c1.date = c2.max_date " +
            "WHERE c1.island_lat IS NOT NULL AND c1.island_lng IS NOT NULL " +
            "ORDER BY c1.island" +
            "</script>")
    List<Coral> getIslandsCoordinatesBatch(List<String> islands);

    /**
     * 查询每个岛屿最新年份的趋势分析元数据
     * @return 趋势分析元数据列表
     */
    @Select("SELECT t1.* FROM trend_analysis_metadata t1 " +
            "INNER JOIN (" +
            "  SELECT island, MAX(date) as max_date " +
            "  FROM trend_analysis_metadata " +
            "  GROUP BY island" +
            ") t2 ON t1.island = t2.island AND t1.date = t2.max_date " +
            "ORDER BY t1.island")
    List<TrendAnalysisMetadata> getLatestYearAllIslandsMetadata();

    /**
     * 批量获取所有岛屿的珊瑚数据
     * @return 珊瑚数据列表
     */
    @Select("select * from coral order by island, date desc")
    List<Coral> getAllIslandsCoralData();
}
