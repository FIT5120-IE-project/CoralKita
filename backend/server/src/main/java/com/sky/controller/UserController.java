package com.sky.controller;


import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import com.sky.vo.UserRegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "User Related APIs")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;


    @PostMapping("/login")
    @ApiOperation(value = "User login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userDTO) {
        log.info("User login: {}", userDTO);

        UserLoginVO userLoginVO = userService.login(userDTO);

        // After successful login, generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, userLoginVO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // Set token to return object
        userLoginVO.setToken(token);
        


        return Result.success(userLoginVO);
    }

    /**
     * User registration
     *
     * @param userRegisterDTO User registration data
     * @return Registration result
     */
    @PostMapping("/register")
    @ApiOperation(value = "User registration")
    public Result<UserRegisterVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("User registration: {}", userRegisterDTO);

        UserRegisterVO userRegisterVO = userService.register(userRegisterDTO);

        // After successful registration, generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, userRegisterVO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // Set token to return object
        userRegisterVO.setToken(token);

        return Result.success(userRegisterVO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "User logout")
    public Result<String> logout() {
        return Result.success();
    }
}
