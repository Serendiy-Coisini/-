package com.campus.retail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.retail.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单 Mapper - 数据访问层
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
