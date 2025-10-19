package com.sky.mapper;

import com.sky.entity.CoralPicture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Coral Picture Mapper Interface
 */
@Mapper
public interface CoralPictureMapper {

    /**
     * Insert coral picture record
     * @param coralPicture Coral picture information
     */
    @Insert("INSERT INTO coral_picture (picture, answer) VALUES (#{picture}, #{answer})")
    void insert(CoralPicture coralPicture);

    /**
     * Query picture list by answer
     * @param answer Answer/classification
     * @return List of pictures
     */
    @Select("SELECT * FROM coral_picture WHERE answer = #{answer}")
    List<CoralPicture> selectByAnswer(String answer);

    /**
     * Query all pictures
     * @return List of pictures
     */
    @Select("SELECT * FROM coral_picture ORDER BY id DESC")
    List<CoralPicture> selectAll();

    /**
     * Randomly query specified number of pictures
     * @param limit Query limit
     * @return List of random pictures
     */
    @Select("SELECT * FROM coral_picture ORDER BY RAND() LIMIT #{limit}")
    List<CoralPicture> selectRandomPictures(int limit);
}
