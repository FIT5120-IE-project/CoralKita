package com.sky.mapper;

import com.sky.entity.Trend;
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
            " order by date desc, island" +
            "</script>")
    List<Trend> getByIslands(List<String> islands);
}
