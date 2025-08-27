package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.Employee;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;
import com.sky.vo.UserRegisterVO;

public interface UserService {

    UserLoginVO login(UserLoginDTO userLoginDTO);


    UserRegisterVO register(UserRegisterDTO userRegisterDTO);

}
