package com.campus.retail.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.retail.entity.User;
import com.campus.retail.mapper.UserMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 用户 Service - 业务逻辑层
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    /** 按学号查询（学号存在 username 字段） */
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> q = new LambdaQueryWrapper<>();
        q.eq(User::getUsername, username);
        return userMapper.selectOne(q);
    }

    public User getByStudentId(String studentId) {
        return getByUsername(studentId);
    }

    /** 注册：学号、密码、宿舍号 */
    public void register(String studentId, String password, String dormitory) {
        if (getByStudentId(studentId) != null) {
            throw new RuntimeException("该学号已注册");
        }
        User u = new User();
        u.setUsername(studentId);
        u.setPassword(password);
        u.setDormitory(dormitory);
        u.setRole("USER");
        userMapper.insert(u);
    }

    public List<User> listAll() {
        return userMapper.selectList(null);
    }

    public boolean save(User user) {
        return userMapper.insert(user) > 0;
    }

    public boolean updateById(User user) {
        return userMapper.updateById(user) > 0;
    }
}
