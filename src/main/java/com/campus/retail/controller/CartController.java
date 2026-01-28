package com.campus.retail.controller;

import com.campus.retail.entity.Product;
import com.campus.retail.entity.User;
import com.campus.retail.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import java.util.*;

/**
 * 购物车 Controller - 加入购物车、查看、修改、删除
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Resource
    private ProductService productService;

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getOrCreateCart(HttpSession session) {
        Object o = session.getAttribute("cart");
        if (o instanceof Map) return (Map<Long, Integer>) o;
        Map<Long, Integer> cart = new HashMap<>();
        session.setAttribute("cart", cart);
        return cart;
    }

    /** 购物车页面 */
    @GetMapping
    public String view(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("ADMIN".equals(user.getRole())) return "redirect:/";
        Map<Long, Integer> cart = getOrCreateCart(session);
        List<CartItem> items = new ArrayList<>();
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> e : cart.entrySet()) {
            Product p = productService.getById(e.getKey());
            if (p != null) {
                CartItem item = new CartItem(p, e.getValue());
                items.add(item);
                total = total.add(item.getSubtotal());
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        return "cart/view";
    }

    /** 加入购物车 */
    @PostMapping("/add")
    public String add(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer quantity,
                      HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("ADMIN".equals(user.getRole())) return "redirect:/";
        Product p = productService.getById(productId);
        if (p == null) { ra.addFlashAttribute("msg", "商品不存在"); return "redirect:/product/list"; }
        if (quantity <= 0) quantity = 1;
        if (p.getStock() < quantity) { ra.addFlashAttribute("msg", "库存不足"); return "redirect:/product/list"; }
        Map<Long, Integer> cart = getOrCreateCart(session);
        int now = cart.getOrDefault(productId, 0) + quantity;
        if (now > p.getStock()) now = p.getStock();
        cart.put(productId, now);
        session.setAttribute("cart", cart);
        ra.addFlashAttribute("msg", "已加入购物车");
        return "redirect:/cart";
    }

    /** 从购物车移除 */
    @GetMapping("/remove/{productId}")
    public String remove(@PathVariable Long productId, HttpSession session) {
        Map<Long, Integer> cart = getOrCreateCart(session);
        cart.remove(productId);
        return "redirect:/cart";
    }

    /** 更新数量 */
    @PostMapping("/update")
    public String update(@RequestParam Long productId, @RequestParam Integer quantity,
                         HttpSession session, RedirectAttributes ra) {
        Map<Long, Integer> cart = getOrCreateCart(session);
        if (quantity == null || quantity <= 0) {
            cart.remove(productId);
        } else {
            Product p = productService.getById(productId);
            if (p != null) {
                int q = Math.min(quantity, p.getStock());
                cart.put(productId, q);
            }
        }
        return "redirect:/cart";
    }

    /** 购物车项（商品+数量） */
    public static class CartItem {
        public final Product product;
        public final int quantity;
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        public java.math.BigDecimal getSubtotal() {
            return product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity));
        }
    }
}
