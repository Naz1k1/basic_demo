package com.naz1k1.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.dto.AddUserDTO;
import com.naz1k1.model.request.dto.UpdateUserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean addUser(AddUserDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto,user,"password");
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return userMapper.insert(user) > 0;
    }

    public boolean deleteUser(int id) {
        return userMapper.deleteById(id) > 0;
    }

    public boolean updateUser(UpdateUserDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto,user,"password");
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return userMapper.updateById(user) > 0;
    }

    public User getById(int id) {
        return userMapper.selectById(id);
    }

    public Page<User> page(Integer pageNum, Integer pageSize) {
        return userMapper.selectPage(new Page<>(pageNum,pageSize),null);
    }

    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userMapper.selectOne(queryWrapper);
    }

}
