package com.campus.retail.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.retail.entity.Product;
import com.campus.retail.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品 Service - 业务逻辑层
 */
@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;

    public List<Product> listAll() {
        return productMapper.selectList(null);
    }

    public List<Product> listByCategory(String category) {
        if (category == null || category.isBlank()) {
            return listAll();
        }
        LambdaQueryWrapper<Product> q = new LambdaQueryWrapper<>();
        q.eq(Product::getCategory, category);
        return productMapper.selectList(q);
    }

    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    public boolean save(Product product) {
        return productMapper.insert(product) > 0;
    }

    public boolean updateById(Product product) {
        return productMapper.updateById(product) > 0;
    }

    public boolean removeById(Long id) {
        return productMapper.deleteById(id) > 0;
    }

    /**
     * 扣减库存
     */
    public boolean reduceStock(Long productId, int quantity) {
        Product p = productMapper.selectById(productId);
        if (p == null || p.getStock() < quantity) {
            return false;
        }
        p.setStock(p.getStock() - quantity);
        return productMapper.updateById(p) > 0;
    }

    /**
     * 获取热门商品（按库存或销量，这里简单返回前6个）
     */
    public List<Product> getHotProducts(int limit) {
        LambdaQueryWrapper<Product> q = new LambdaQueryWrapper<>();
        q.orderByDesc(Product::getStock).last("LIMIT " + limit);
        return productMapper.selectList(q);
    }

    /**
     * 获取所有不重复的分类列表
     */
    public List<String> getAllCategories() {
        List<Product> allProducts = productMapper.selectList(null);
        return allProducts.stream()
                .map(Product::getCategory)
                .filter(cat -> cat != null && !cat.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
