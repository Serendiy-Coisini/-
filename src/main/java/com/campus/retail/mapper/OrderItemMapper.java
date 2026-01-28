package com.campus.retail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.retail.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单明细 Mapper - 数据访问层
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
