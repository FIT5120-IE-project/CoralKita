package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT token validation interceptor
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Validate JWT token
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the intercepted request is a Controller method or other resource
        if (!(handler instanceof HandlerMethod)) {
            // Not a dynamic method, allow access
            return true;
        }

        // 1. Get token from request header
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        // 2. Validate token
        try {
            log.info("JWT validation: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("Current employee ID: ", empId);
            // 3. Pass, allow access
            return true;
        } catch (Exception ex) {
            // 4. Fail, return 401 status code
            response.setStatus(401);
            return false;
        }
    }
}
