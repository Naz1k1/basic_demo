package com.naz1k1.config;

import com.naz1k1.model.request.dto.AddUserDTO;
import com.naz1k1.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialDataConfig {

    private final UserService userService;

    public InitialDataConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public CommandLineRunner initializeData( PasswordEncoder passwordEncoder) {
        return args -> {
            // 检查是否已存在管理员用户
            if (userService.getByUsername("admin") == null) {
                // 创建管理员用户
                AddUserDTO adminDto = new AddUserDTO();
                adminDto.setUsername("admin");
                adminDto.setPassword(passwordEncoder.encode("123456"));
                adminDto.setEmail("admin@example.com");
                userService.addUser(adminDto);
            }
        };
    }
}