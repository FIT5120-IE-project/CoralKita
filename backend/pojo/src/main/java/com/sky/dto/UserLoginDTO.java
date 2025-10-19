package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Client User Login DTO
 */
@Data
public class UserLoginDTO implements Serializable {

    private String name;
    private String password;

}
