package com.sky.mapper;

import com.sky.entity.CoralPicture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 珊瑚图片Mapper接口
 */
@Mapper
public interface CoralPictureMapper {

    /**
     * 插入珊瑚图片记录
     * @param coralPicture 珊瑚图片信息
     */
    @Insert("INSERT INTO coral_picture (picture, answer) VALUES (#{picture}, #{answer})")
    void insert(CoralPicture coralPicture);

    /**
     * 根据答案查询图片列表
     * @param answer 答案/分类
     * @return 图片列表
     */
    @Select("SELECT * FROM coral_picture WHERE answer = #{answer}")
    List<CoralPicture> selectByAnswer(String answer);

    /**
     * 查询所有图片
     * @return 图片列表
     */
    @Select("SELECT * FROM coral_picture ORDER BY id DESC")
    List<CoralPicture> selectAll();

    /**
     * 随机查询指定数量的图片
     * @param limit 查询数量
     * @return 随机图片列表
     */
    @Select("SELECT * FROM coral_picture ORDER BY RAND() LIMIT #{limit}")
    List<CoralPicture> selectRandomPictures(int limit);
}
