package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param name
     * @return
     */
    @Select("select * from user where name = #{name}")
    User getByUsername(String name);

    /**
     * 根据用户ID查询用户
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getById(Integer id);

    /**
     * 新增用户
     * @param user
     */
    @Insert("insert into user(name, password) values(#{name}, #{password})")
    void insert(User user);

    /**
     * 更新用户得分和等级
     * @param user
     */
    @Update("update user set points = #{points}, level = #{level}, exp = #{exp} where id = #{id}")
    void updateUserScore(User user);

}
