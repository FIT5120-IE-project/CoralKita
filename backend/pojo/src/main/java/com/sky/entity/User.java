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

    // WeChat user unique identifier
    private String openid;

    // Name
    private String name;

    // Phone number
    private String phone;

    // Gender: 0 female, 1 male
    private String sex;

    // ID number
    private String idNumber;

    // Avatar
    private String avatar;

    // Registration time
    private LocalDateTime createTime;
}
**/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{

    // User ID
    private int id;

    // Nickname
    private String name;

    // Password
    private String password;

    // Points
    private int points;

    // Badges
    private String badges;

    // Level (text type in database)
    private String level;

    // Experience
    private int exp;

    // Email
    private String email;
}
