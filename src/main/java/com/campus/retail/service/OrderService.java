package com.campus.retail.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.retail.entity.OrderItem;
import com.campus.retail.entity.Orders;
import com.campus.retail.entity.Product;
import com.campus.retail.mapper.OrderItemMapper;
import com.campus.retail.mapper.OrdersMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 订单 Service - 业务逻辑层
 */
@Service
public class OrderService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductService productService;

    public List<Orders> listByUserId(Long userId) {
        LambdaQueryWrapper<Orders> q = new LambdaQueryWrapper<>();
        q.eq(Orders::getUserId, userId).orderByDesc(Orders::getCreateTime);
        return ordersMapper.selectList(q);
    }

    public List<Orders> listAll() {
        LambdaQueryWrapper<Orders> q = new LambdaQueryWrapper<>();
        q.orderByDesc(Orders::getCreateTime);
        return ordersMapper.selectList(q);
    }

    public Orders getById(Long id) {
        return ordersMapper.selectById(id);
    }

    public List<OrderItem> listItemsByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderItem> q = new LambdaQueryWrapper<>();
        q.eq(OrderItem::getOrderId, orderId);
        return orderItemMapper.selectList(q);
    }

    /**
     * 创建订单（商品ID -> 数量），收货信息：姓名、宿舍号、联系电话
     */
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(Long userId, Map<Long, Integer> productIdToQty,
                             String receiverName, String receiverDormitory, String receiverPhone) {
        if (productIdToQty == null || productIdToQty.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }
        if (receiverName == null || receiverName.isBlank()) throw new RuntimeException("请填写收货人姓名");
        if (receiverDormitory == null || receiverDormitory.isBlank()) throw new RuntimeException("请填写宿舍号");
        if (receiverPhone == null || receiverPhone.isBlank()) throw new RuntimeException("请填写联系电话");

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> e : productIdToQty.entrySet()) {
            Product p = productService.getById(e.getKey());
            if (p == null) throw new RuntimeException("商品不存在: " + e.getKey());
            if (p.getStock() < e.getValue()) {
                throw new RuntimeException("商品库存不足: " + p.getName());
            }
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
        }

        String orderNo = "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setReceiverName(receiverName.trim());
        order.setReceiverDormitory(receiverDormitory.trim());
        order.setReceiverPhone(receiverPhone.trim());
        order.setStatus("PENDING");
        ordersMapper.insert(order);

        for (Map.Entry<Long, Integer> e : productIdToQty.entrySet()) {
            Product p = productService.getById(e.getKey());
            BigDecimal amount = p.getPrice().multiply(BigDecimal.valueOf(e.getValue()));
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setPrice(p.getPrice());
            item.setQuantity(e.getValue());
            item.setAmount(amount);
            orderItemMapper.insert(item);
            productService.reduceStock(p.getId(), e.getValue());
        }

        return order;
    }

    public boolean payOrder(Long orderId) {
        Orders o = ordersMapper.selectById(orderId);
        if (o == null || !"PENDING".equals(o.getStatus())) return false;
        o.setStatus("PAID");
        return ordersMapper.updateById(o) > 0;
    }

    public boolean cancelOrder(Long orderId) {
        Orders o = ordersMapper.selectById(orderId);
        if (o == null || !"PENDING".equals(o.getStatus())) return false;
        o.setStatus("CANCELLED");
        return ordersMapper.updateById(o) > 0;
    }
}
