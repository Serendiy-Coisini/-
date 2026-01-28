package com.campus.retail.controller;

import com.campus.retail.entity.User;
import com.campus.retail.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面 Controller - 首页、登录页
 */
@Controller
public class PageController {

    @Resource
    private ProductService productService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // 只有普通用户才显示热门商品，管理员不显示
        if (!"ADMIN".equals(user.getRole())) {
            model.addAttribute("hotProducts", productService.getHotProducts(6));
        }
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
