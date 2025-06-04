package com.naz1k1.service;

import com.naz1k1.entity.User;
import com.naz1k1.model.request.dto.LoginDTO;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
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

    public User login(LoginDTO dto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        HttpSession session = request.getSession(true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        session.setMaxInactiveInterval(30 * 24 * 60 * 60);

        return (User) authentication.getPrincipal();
    }

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

}
