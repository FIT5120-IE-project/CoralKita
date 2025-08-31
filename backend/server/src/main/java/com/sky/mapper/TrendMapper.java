package com.sky.mapper;

import com.sky.entity.Trend;
import com.sky.entity.Coral;
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
}
