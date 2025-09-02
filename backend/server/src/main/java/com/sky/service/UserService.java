package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.vo.UserLoginVO;
import com.sky.vo.UserRegisterVO;

public interface UserService {

    UserLoginVO login(UserLoginDTO userLoginDTO);

    UserRegisterVO register(UserRegisterDTO userRegisterDTO);

    /**
     * 更新用户徽章
     * @param userId 用户ID
     * @param badge 徽章名称
     */
    void updateUserBadge(Integer userId, String badge);
}
