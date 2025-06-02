package com.naz1k1.controller;

import com.naz1k1.entity.User;
import com.naz1k1.model.Result;
import com.naz1k1.model.request.dto.AddUserDTO;
import com.naz1k1.model.request.dto.UpdateUserDTO;
import com.naz1k1.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public Result<?> addUser(@RequestBody AddUserDTO dto) {
        if (userService.addUser(dto)) {
            return Result.success();
        }
        return Result.fail();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id)) {
            return Result.success();
        }
        return Result.fail();
    }

    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody UpdateUserDTO dto) {
        if (userService.updateUser(dto)) {
            return Result.success();
        }
        return Result.fail();
    }

    @GetMapping("/get/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.fail();
    }

    @GetMapping("/get/page")
    public Result<?> getByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(userService.page(pageNum,pageSize));
    }

    @GetMapping("/get/username")
    public Result<?> getByUsername(@RequestParam String username) {
        return Result.success(userService.getByUsername(username));
    }
}
