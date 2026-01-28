package com.campus.retail.controller;

import com.campus.retail.entity.OrderItem;
import com.campus.retail.entity.Orders;
import com.campus.retail.entity.Product;
import com.campus.retail.entity.User;
import com.campus.retail.service.OrderService;
import com.campus.retail.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订单 Controller - 下单、我的订单、订单管理
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ProductService productService;

    /** 我的订单 */
    @GetMapping("/my")
    public String myOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Orders> list = orderService.listByUserId(user.getId());
        model.addAttribute("orders", list);
        model.addAttribute("user", user);
        return "order/my";
    }

    /** 订单详情 */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Orders order = orderService.getById(id);
        if (order == null) {
            return "redirect:/order/my";
        }
        if (!order.getUserId().equals(user.getId()) && !"ADMIN".equals(user.getRole())) {
            return "redirect:/order/my";
        }
        List<OrderItem> items = orderService.listItemsByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("user", user);
        return "order/detail";
    }

    /** 确认订单页面（填写收货信息） */
    @GetMapping("/confirm")
    @SuppressWarnings("unchecked")
    public String confirm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("ADMIN".equals(user.getRole())) return "redirect:/";
        Object co = session.getAttribute("cart");
        Map<Long, Integer> cart = (co instanceof Map) ? (Map<Long, Integer>) co : null;
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        // 计算总金额和商品列表
        List<Product> products = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> e : cart.entrySet()) {
            Product p = productService.getById(e.getKey());
            if (p != null) {
                products.add(p);
                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
            }
        }
        model.addAttribute("cart", cart);
        model.addAttribute("products", products);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        return "order/confirm";
    }

    /** 提交订单（从确认订单页面，需填写姓名、宿舍号、联系电话） */
    @PostMapping("/submit")
    @SuppressWarnings("unchecked")
    public String submit(@RequestParam String receiverName,
                         @RequestParam String dormitoryArea,
                         @RequestParam String dormitoryBuilding,
                         @RequestParam String dormitoryNumber,
                         @RequestParam String receiverPhone,
                         HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        
        // 验证宿舍信息
        if (dormitoryArea == null || dormitoryArea.isBlank()) {
            ra.addFlashAttribute("msg", "请选择宿舍区域");
            return "redirect:/order/confirm";
        }
        if (dormitoryBuilding == null || dormitoryBuilding.isBlank()) {
            ra.addFlashAttribute("msg", "请选择栋号");
            return "redirect:/order/confirm";
        }
        if (dormitoryNumber == null || dormitoryNumber.isBlank()) {
            ra.addFlashAttribute("msg", "请填写宿舍号");
            return "redirect:/order/confirm";
        }
        
        // 验证电话号码
        if (receiverPhone == null || receiverPhone.isBlank()) {
            ra.addFlashAttribute("msg", "请填写联系电话");
            return "redirect:/order/confirm";
        }
        receiverPhone = receiverPhone.trim();
        if (receiverPhone.length() != 11 || !receiverPhone.matches("\\d{11}")) {
            ra.addFlashAttribute("msg", "联系电话必须为11位数字");
            return "redirect:/order/confirm";
        }
        
        // 组合宿舍信息：区域+栋号+宿舍号，如"南一区A栋301"
        String receiverDormitory = dormitoryArea.trim() + dormitoryBuilding.trim() + "栋" + dormitoryNumber.trim();
        
        Object co = session.getAttribute("cart");
        Map<Long, Integer> cart = (co instanceof Map) ? (Map<Long, Integer>) co : null;
        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("msg", "购物车为空，请先添加商品");
            return "redirect:/cart";
        }
        try {
            Orders o = orderService.createOrder(user.getId(), cart, receiverName, receiverDormitory, receiverPhone);
            session.removeAttribute("cart");
            ra.addFlashAttribute("msg", "下单成功，订单号：" + o.getOrderNo());
        } catch (Exception e) {
            ra.addFlashAttribute("msg", "下单失败：" + e.getMessage());
            return "redirect:/order/confirm";
        }
        return "redirect:/order/my";
    }

    /** 支付 */
    @GetMapping("/pay/{id}")
    public String pay(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        orderService.payOrder(id);
        ra.addFlashAttribute("msg", "支付成功");
        return "redirect:/order/detail/" + id;
    }

    /** 取消 */
    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        orderService.cancelOrder(id);
        ra.addFlashAttribute("msg", "订单已取消");
        return "redirect:/order/my";
    }

    /** 管理端：全部订单 */
    @GetMapping("/manage")
    public String manage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        model.addAttribute("orders", orderService.listAll());
        model.addAttribute("user", user);
        return "order/manage";
    }
}
