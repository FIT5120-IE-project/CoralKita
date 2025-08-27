package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

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
     * 新增用户
     * @param user
     */
    @Insert("insert into user(name, password) values(#{name}, #{password})")
    void insert(User user);

}
