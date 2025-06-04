package com.naz1k1.service;

import com.naz1k1.entity.User;
import com.naz1k1.model.request.dto.LoginDTO;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 用户登录
     * @param dto 登录信息
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 登录用户信息
     */
    public User login(LoginDTO dto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1. 创建认证token
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            // 2. 认证
            Authentication authentication = authenticationManager.authenticate(authRequest);

            // 3. 创建新的session并存储认证信息
            HttpSession session = request.getSession(true);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            // 4. 设置session过期时间
            if (dto.getRememberMe()) {
                session.setMaxInactiveInterval(30 * 24 * 60 * 60); // 30天
            } else {
                session.setMaxInactiveInterval(2 * 60 * 60); // 2小时
            }

            // 5. 返回用户信息
            return (User) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("用户名或密码错误");
        }
    }

    /**
     * 用户注销
     * @param request HTTP请求
     * @param response HTTP响应
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取当前session
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 2. 清除session中的认证信息
            session.removeAttribute("SPRING_SECURITY_CONTEXT");
            // 3. 使session失效
            session.invalidate();
        }

        // 4. 清除SecurityContextHolder中的认证信息
        SecurityContextHolder.clearContext();
    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户信息
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

}
