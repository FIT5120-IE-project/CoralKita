package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * Query user by username
     * @param name Username
     * @return User object
     */
    @Select("select * from user where name = #{name}")
    User getByUsername(String name);

    /**
     * Query user by user ID
     * @param id User ID
     * @return User object
     */
    @Select("select * from user where id = #{id}")
    User getById(Integer id);

    /**
     * Insert new user
     * @param user User object
     */
    @Insert("insert into user(name, password, email, points, level, exp) values(#{name}, #{password}, #{email}, #{points}, #{level}, #{exp})")
    void insert(User user);

    /**
     * Update user score and level
     * @param user User object
     */
    @Update("update user set points = #{points}, level = #{level}, exp = #{exp} where id = #{id}")
    void updateUserScore(User user);

    /**
     * Update user badge
     * @param userId User ID
     * @param badge Badge name
     */
    @Update("update user set badges = #{badge} where id = #{userId}")
    void updateUserBadge(Integer userId, String badge);

}
