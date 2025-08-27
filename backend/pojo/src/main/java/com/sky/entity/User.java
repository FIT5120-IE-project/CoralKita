package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //微信用户唯一标识
    private String openid;

    //姓名
    private String name;

    //手机号
    private String phone;

    //性别 0 女 1 男
    private String sex;

    //身份证号
    private String idNumber;

    //头像
    private String avatar;

    //注册时间
    private LocalDateTime createTime;
}
**/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{

    //用户 id
    private int id;

    //昵称
    private String name;

    //密码
    private String password;

    //积分
    private int points;

    //勋章
    private String badges;

    //等级
    private String level;

    //经验
    private int experience;
}
