package com.naz1k1.controller;

import com.naz1k1.entity.User;
import com.naz1k1.model.Result;
import com.naz1k1.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(@AuthenticationPrincipal User user) {
        return Result.success(user);
    }

    public Result<?> login() {
        return Result.success();
    }
}
