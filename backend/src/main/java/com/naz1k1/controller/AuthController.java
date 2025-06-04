package com.naz1k1.controller;

import com.naz1k1.entity.User;
import com.naz1k1.model.Result;
import com.naz1k1.model.request.dto.LoginDTO;
import com.naz1k1.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @PostMapping("/login")
    public Result<User> login(@RequestBody @Valid LoginDTO loginDTO,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        User user = authService.login(loginDTO, request, response);
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request,
                               HttpServletResponse response) {
        authService.logout(request, response);
        return Result.success(null);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser() {
        User user = authService.getCurrentUser();
        return Result.success(user);
    }
}
