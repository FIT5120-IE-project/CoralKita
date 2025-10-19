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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * User login
     *
     * @param user1 User login DTO
     * @return User login VO
     */
    public UserLoginVO login(UserLoginDTO user1) {
        String username = user1.getName();
        String password = user1.getPassword();

        // 1. Query database by username
        User user = userMapper.getByUsername(username);

        // 2. Handle various exception cases (username doesn't exist, wrong password, account locked)
        if (user == null) {
            // Account doesn't exist
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // Password comparison
        // TODO Need to perform MD5 encryption in the future, then compare
        if (!password.equals(user.getPassword())) {
            // Wrong password
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 3. Build return object
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .points(user.getPoints())
                .badges(user.getBadges())
                .level(user.getLevel())
                .experience(user.getExp())
                .build();

        return userLoginVO;
    }

    /**
     * User registration
     *
     * @param userRegisterDTO User registration DTO
     * @return User registration VO
     */
    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getName();
        String password = userRegisterDTO.getPassword();

        // 1. Parameter validation
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        // 2. Check if username already exists
        User existingUser = userMapper.getByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        // 3. Create new user
        User user = new User();
        user.setName(username);
        // Perform MD5 encryption on password
        user.setPassword(password);
        user.setEmail(userRegisterDTO.getEmail());
        // Set initial values
        user.setPoints(0);
        user.setLevel("Novice");
        user.setExp(0);

        // 4. Save to database (only save name and password, other fields use database defaults)
        userMapper.insert(user);

        // 5. Re-query user information (get database-generated ID and default values)
        User savedUser = userMapper.getByUsername(username);

        // 6. Build return object
        UserRegisterVO userRegisterVO = UserRegisterVO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .points(savedUser.getPoints())
                .badges(savedUser.getBadges())
                .level(savedUser.getLevel())
                .experience(savedUser.getExp())
                .build();

        return userRegisterVO;
    }

    /**
     * Update user badge
     *
     * @param userId User ID
     * @param badge  Badge name
     */
    @Override
    public void updateUserBadge(Integer userId, String badge) {
        userMapper.updateUserBadge(userId, badge);
    }
}
