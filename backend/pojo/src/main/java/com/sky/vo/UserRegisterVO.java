package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterVO implements Serializable {

    private Integer id;
    private String name;
    private String email;
    private Integer points;
    private String badges;
    private String level;
    private Integer experience;
    private String token;
}
