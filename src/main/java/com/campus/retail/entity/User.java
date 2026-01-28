package com.campus.retail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体 - 对应 user 表
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;   // 学号（登录用）
    private String password;
    private String realName;
    private String phone;
    private String dormitory;  // 宿舍号
    private String role;       // ADMIN / USER
    private LocalDateTime createTime;
}
