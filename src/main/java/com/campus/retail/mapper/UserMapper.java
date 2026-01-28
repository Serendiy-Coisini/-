package com.campus.retail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.retail.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper - 数据访问层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
