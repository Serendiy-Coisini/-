/*
 Navicat Premium Dump SQL

 Source Server         : javaee
 Source Server Type    : MySQL
 Source Server Version : 80407 (8.4.7)
 Source Host           : localhost:3306
 Source Schema         : campus_retail

 Target Server Type    : MySQL
 Target Server Version : 80407 (8.4.7)
 File Encoding         : 65001

 Date: 29/01/2026 02:40:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称（冗余）',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 1, '矿泉水', 2.50, 6, 15.00);
INSERT INTO `order_item` VALUES (2, 1, 2, '方便面', 5.00, 2, 10.00);
INSERT INTO `order_item` VALUES (3, 1, 5, '酸奶', 6.50, 1, 6.50);
INSERT INTO `order_item` VALUES (4, 2, 1, '矿泉水', 2.50, 1, 2.50);
INSERT INTO `order_item` VALUES (5, 3, 1, '矿泉水', 2.50, 1, 2.50);
INSERT INTO `order_item` VALUES (6, 3, 2, '方便面', 5.00, 1, 5.00);
INSERT INTO `order_item` VALUES (7, 4, 1, '矿泉水', 2.50, 1, 2.50);
INSERT INTO `order_item` VALUES (8, 4, 2, '方便面', 5.00, 1, 5.00);
INSERT INTO `order_item` VALUES (9, 4, 5, '酸奶', 6.50, 1, 6.50);
INSERT INTO `order_item` VALUES (10, 5, 1, '矿泉水', 2.50, 1, 2.50);
INSERT INTO `order_item` VALUES (11, 5, 2, '方便面', 5.00, 1, 5.00);
INSERT INTO `order_item` VALUES (12, 5, 4, '笔记本', 3.00, 2, 6.00);
INSERT INTO `order_item` VALUES (13, 5, 7, '三明治', 7.00, 1, 7.00);
INSERT INTO `order_item` VALUES (14, 6, 1, '矿泉水', 2.50, 1, 2.50);
INSERT INTO `order_item` VALUES (15, 6, 2, '方便面', 5.00, 1, 5.00);
INSERT INTO `order_item` VALUES (16, 6, 4, '笔记本', 3.00, 1, 3.00);
INSERT INTO `order_item` VALUES (17, 6, 5, '酸奶', 6.50, 2, 13.00);
INSERT INTO `order_item` VALUES (18, 6, 6, '辣条', 4.00, 1, 4.00);
INSERT INTO `order_item` VALUES (19, 6, 7, '三明治', 7.00, 1, 7.00);
INSERT INTO `order_item` VALUES (20, 6, 8, '可乐', 3.00, 1, 3.00);
INSERT INTO `order_item` VALUES (21, 6, 9, '奶酪片', 8.00, 1, 8.00);
INSERT INTO `order_item` VALUES (22, 7, 1, '矿泉水', 2.50, 1, 2.50);
INSERT INTO `order_item` VALUES (23, 7, 2, '方便面', 5.00, 3, 15.00);
INSERT INTO `order_item` VALUES (24, 7, 5, '酸奶', 6.50, 1, 6.50);
INSERT INTO `order_item` VALUES (25, 8, 1, '矿泉水', 2.50, 66, 165.00);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_dormitory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'PENDING' COMMENT '状态：PENDING-待支付, PAID-已支付, CANCELLED-已取消',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '单价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片路径',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '矿泉水', 2.50, 22, '纯净饮用水', '饮料', '/uploads/product/1.jpg', '2026-01-26 14:46:14', '2026-01-26 14:46:14');
INSERT INTO `product` VALUES (2, '方便面', 5.00, 71, '香辣牛肉面', '食品', '/uploads/product/2.jpg', '2026-01-26 14:46:14', '2026-01-26 14:46:14');
INSERT INTO `product` VALUES (4, '笔记本', 3.00, 10, 'A5规格', '日用品', '/uploads/product/4.jpg', '2026-01-26 14:46:14', '2026-01-26 14:46:14');
INSERT INTO `product` VALUES (5, '酸奶', 6.50, 55, '卡士酸奶', '饮料', '/uploads/product/5.jpg', '2026-01-26 14:46:14', '2026-01-26 14:46:14');
INSERT INTO `product` VALUES (6, '辣条', 4.00, 29, '卫龙辣条', '食品', '/uploads/product/6.jpg', '2026-01-26 15:58:28', '2026-01-26 15:58:28');
INSERT INTO `product` VALUES (7, '三明治', 7.00, 28, '包装三明治', '食品', '/uploads/product/7.jpg', '2026-01-26 16:00:48', '2026-01-26 16:00:49');
INSERT INTO `product` VALUES (8, '可乐', 3.00, 49, '可乐', '饮料', '/uploads/product/8.jpg', '2026-01-26 16:01:59', '2026-01-26 16:01:59');
INSERT INTO `product` VALUES (9, '奶酪片', 8.00, 19, '奶酪片', '奶制品', '/uploads/product/9.jpg', '2026-01-26 16:09:18', '2026-01-26 16:09:18');
INSERT INTO `product` VALUES (10, '乐事薯片', 5.00, 20, '乐事薯片-原味', '膨化食品', '/uploads/product/10.jpg', '2026-01-29 02:12:42', '2026-01-29 02:12:42');
INSERT INTO `product` VALUES (11, '纸巾', 4.00, 20, '维达纸巾', '日用品', '/uploads/product/11.jpg', '2026-01-29 02:13:01', '2026-01-29 02:13:01');
INSERT INTO `product` VALUES (12, '炫迈口香糖', 8.00, 10, '炫迈口香糖-葡萄味', '糖果', '/uploads/product/12.jpg', '2026-01-29 02:13:30', '2026-01-29 02:13:30');
INSERT INTO `product` VALUES (13, '百威啤酒', 6.00, 20, '百威啤酒', '酒', '/uploads/product/13.jpg', '2026-01-29 02:14:10', '2026-01-29 02:14:10');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号（登录用）',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `dormitory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员, USER-普通用户',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', '系统管理员', NULL, NULL, 'ADMIN', '2026-01-26 14:46:14');
INSERT INTO `user` VALUES (2, '2021001', '123456', '张三', NULL, '1栋301', 'USER', '2026-01-26 14:46:14');
INSERT INTO `user` VALUES (3, '2021002', '123456', '李四', NULL, '2栋205', 'USER', '2026-01-26 14:46:14');

SET FOREIGN_KEY_CHECKS = 1;
