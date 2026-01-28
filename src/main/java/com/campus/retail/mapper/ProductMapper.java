package com.campus.retail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.retail.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品 Mapper - 数据访问层
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
