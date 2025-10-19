package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.vo.UserLoginVO;
import com.sky.vo.UserRegisterVO;

public interface UserService {

    UserLoginVO login(UserLoginDTO userLoginDTO);

    UserRegisterVO register(UserRegisterDTO userRegisterDTO);

    /**
     * Update user badge
     * @param userId User ID
     * @param badge Badge name
     */
    void updateUserBadge(Integer userId, String badge);
}
