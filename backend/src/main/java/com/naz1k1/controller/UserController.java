package com.naz1k1.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naz1k1.entity.User;
import com.naz1k1.model.Result;
import com.naz1k1.model.request.dto.AddUserDTO;
import com.naz1k1.model.request.dto.UpdateUserDTO;
import com.naz1k1.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Result<Boolean> addUser(@RequestBody @Valid AddUserDTO dto) {
        boolean success = userService.addUser(dto);
        return Result.success(success);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable("id") @Min(1) Integer id) {
        boolean success = userService.deleteUser(id);
        return Result.success(success);
    }

    @PutMapping
    public Result<Boolean> updateUser(@RequestBody @Valid UpdateUserDTO dto) {
        boolean success = userService.updateUser(dto);
        return Result.success(success);
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable("id") @Min(1) Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @GetMapping("/page")
    public Result<Page<User>> page(
            @RequestParam(defaultValue = "1") @Min(1) Integer pageNum,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize) {
        Page<User> page = userService.page(pageNum, pageSize);
        return Result.success(page);
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateStatus(
            @PathVariable("id") @Min(1) Integer id,
            @RequestParam @Min(0) Integer status) {
        boolean success = userService.updateStatus(id, status);
        return Result.success(success);
    }
}
