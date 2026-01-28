package com.campus.retail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体 - 对应 orders 表（表名order是关键字，故用orders）
 */
@Data
@TableName("orders")
public class Orders {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private String receiverName;    // 收货人姓名
    private String receiverDormitory;// 宿舍号
    private String receiverPhone;   // 联系电话
    private String status;   // PENDING-待支付, PAID-已支付, CANCELLED-已取消
    private LocalDateTime createTime;
}
