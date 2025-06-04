package com.naz1k1.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.dto.AddUserDTO;
import com.naz1k1.model.request.dto.UpdateUserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordService passwordService;

    public UserService(UserMapper userMapper, PasswordService passwordService) {
        this.userMapper = userMapper;
        this.passwordService = passwordService;
    }

    /**
     * 添加用户
     * @param dto 用户信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(AddUserDTO dto) {
        if (checkUsernameExists(dto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (checkEmailExists(dto.getEmail())) {
            throw new RuntimeException("邮箱也被注册");
        }

        User user = new User();
        BeanUtils.copyProperties(dto,user,"password");
        user.setPasswordHash(passwordService.encodePassword(dto.getPassword()));
        user.setStatus(1);
        return userMapper.insert(user) > 0;
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(int id) {
        return userMapper.deleteById(id) > 0;
    }

    /**
     * 更新用户信息
     * @param dto 用户信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UpdateUserDTO dto) {
        User existingUser = userMapper.selectById(dto.getUserId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名唯一性
        if (!existingUser.getUsername().equals(dto.getUsername())
                && checkUsernameExists(dto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱唯一性
        if (!existingUser.getEmail().equals(dto.getEmail())
                && checkEmailExists(dto.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(dto,user,"password");
        user.setPasswordHash(passwordService.encodePassword(dto.getPassword()));
        return userMapper.updateById(user) > 0;
    }

    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    public User getById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 分页查询用户
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public Page<User> page(Integer pageNum, Integer pageSize) {
        return userMapper.selectPage(new Page<>(pageNum,pageSize),null);
    }

    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    private boolean checkUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    private boolean checkEmailExists(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 启用/禁用用户
     * @param userId 用户ID
     * @param status 状态（0-禁用，1-启用）
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Integer userId, Integer status) {
        if (status != 0 && status != 1) {
            throw new RuntimeException("无效的状态值");
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId)
                .set("status", status);

        return userMapper.update(null, updateWrapper) > 0;
    }



}
