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
     * Query all trend data by island name
     * @param island Island name
     * @return List of trend data
     */
    @Select("select * from trend where island = #{island} order by date desc")
    List<Trend> getByIsland(String island);

    /**
     * Query trend data by multiple island names
     * @param islands List of island names
     * @return List of trend data
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
     * Get all island list
     * @return Island list
     */
    @Select("select distinct island from trend order by island")
    List<String> getAllIslands();

    /**
     * Query coral data by island name, sorted by date
     * @param island Island name
     * @return List of coral data
     */
    @Select("select * from coral where island = #{island} order by date desc")
    List<Coral> getCoralByIslandOrderByDate(String island);

    /**
     * Batch get coordinates of multiple islands (get latest record for each island)
     * @param islands List of island names
     * @return List of coral data, containing latest coordinates for each island
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
     * Query trend analysis metadata for latest year of each island
     * @return List of trend analysis metadata
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
     * Batch get coral data for all islands
     * @return List of coral data
     */
    @Select("select * from coral order by island, date desc")
    List<Coral> getAllIslandsCoralData();
}
