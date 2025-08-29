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
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userDTO) {
        log.info("用户登录：{}", userDTO);

        UserLoginVO userLoginVO = userService.login(userDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, userLoginVO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //设置token到返回对象中
        userLoginVO.setToken(token);

        return Result.success(userLoginVO);
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<UserRegisterVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);

        UserRegisterVO userRegisterVO = userService.register(userRegisterDTO);

        //注册成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, userRegisterVO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //设置token到返回对象中
        userRegisterVO.setToken(token);

        return Result.success(userRegisterVO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出")
    public Result<String> logout() {
        return Result.success();
    }

}
