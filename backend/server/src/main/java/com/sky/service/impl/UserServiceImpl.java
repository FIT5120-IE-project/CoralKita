package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;

import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import com.sky.vo.UserRegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param user1
     * @return
     */
    public UserLoginVO login(UserLoginDTO user1) {
        String username = user1.getName();
        String password = user1.getPassword();

        //1、根据用户名查询数据库中的数据
        User user = userMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、构建返回对象
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .points(user.getPoints())
                .badges(user.getBadges())
                .level(user.getLevel())
                .experience(user.getExp())
                .build();

        return userLoginVO;
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getName();
        String password = userRegisterDTO.getPassword();

        //1、参数校验
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        //2、检查用户名是否已存在
        User existingUser = userMapper.getByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        //3、创建新用户
        User user = new User();
        user.setName(username);
        // 对密码进行MD5加密
        user.setPassword(password);
        // 设置初始值
        user.setPoints(0);
        user.setLevel("Novice");
        user.setExp(0);

        //4、保存到数据库（只保存name和password，其他字段使用数据库默认值）
        userMapper.insert(user);

        //5、重新查询用户信息（获取数据库生成的ID和默认值）
        User savedUser = userMapper.getByUsername(username);

        //6、构建返回对象
        UserRegisterVO userRegisterVO = UserRegisterVO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .points(savedUser.getPoints())
                .badges(savedUser.getBadges())
                .level(savedUser.getLevel())
                .experience(savedUser.getExp())
                .build();

        return userRegisterVO;
    }

}
