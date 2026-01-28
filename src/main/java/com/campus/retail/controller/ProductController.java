package com.campus.retail.controller;

import com.campus.retail.entity.Product;
import com.campus.retail.entity.User;
import com.campus.retail.service.ProductService;
import com.campus.retail.util.ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品 Controller - 商品列表、管理（增删改）
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /** 商品列表（顾客选购，管理员不显示此功能并禁止访问） */
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String category,
                      HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("ADMIN".equals(user.getRole())) return "redirect:/";
        List<Product> list = productService.listByCategory(category);
        List<String> categories = productService.getAllCategories();
        model.addAttribute("products", list);
        model.addAttribute("user", user);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        return "product/list";
    }

    /** 管理端：商品列表 */
    @GetMapping("/manage")
    public String manage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("products", productService.listAll());
        model.addAttribute("user", user);
        return "product/manage";
    }

    /** 跳转新增页 */
    @GetMapping("/add")
    public String addPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        model.addAttribute("user", user);
        return "product/edit";
    }

    /** 跳转编辑页 */
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("user", user);
        return "product/edit";
    }

    /** 保存（新增或更新），支持图片上传/更换 */
    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long id,
                      @RequestParam String name,
                      @RequestParam BigDecimal price,
                      @RequestParam Integer stock,
                      @RequestParam(required = false) String description,
                      @RequestParam(required = false) String category,
                      @RequestParam(required = false) MultipartFile image,
                      HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        Product p;
        if (id != null && id > 0) {
            p = productService.getById(id);
            if (p == null) { ra.addFlashAttribute("msg", "商品不存在"); return "redirect:/product/manage"; }
        } else {
            p = new Product();
        }
        p.setName(name);
        p.setPrice(price);
        p.setStock(stock == null ? 0 : stock);
        p.setDescription(description);
        p.setCategory(category);
        if (id != null && id > 0) {
            productService.updateById(p);
        } else {
            productService.save(p);
        }
        if (image != null && !image.isEmpty()) {
            try {
                // 使用项目根目录的绝对路径
                String projectRoot = System.getProperty("user.dir");
                File uploadDir = new File(projectRoot, "uploads/product");
                uploadDir.mkdirs();
                
                // 获取文件扩展名
                String ext = "";
                String on = image.getOriginalFilename();
                if (on != null && on.contains(".")) {
                    ext = on.substring(on.lastIndexOf("."));
                }
                // 统一使用 .jpg 格式保存（压缩后）
                String fn = p.getId() + ".jpg";
                File dest = new File(uploadDir, fn);
                
                // 压缩并保存图片（商品图片压缩到800x800，质量0.8）
                ImageUtil.compressAndSaveProductImage(image, dest);
                
                p.setImageUrl("/uploads/product/" + fn);
                productService.updateById(p);
            } catch (IOException e) {
                ra.addFlashAttribute("msg", "保存成功，图片上传/压缩失败：" + e.getMessage());
                return "redirect:/product/manage";
            } catch (Exception e) {
                ra.addFlashAttribute("msg", "保存成功，图片处理失败：" + e.getMessage());
                return "redirect:/product/manage";
            }
        }
        ra.addFlashAttribute("msg", "保存成功");
        return "redirect:/product/manage";
    }

    /** 删除 */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        productService.removeById(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/product/manage";
    }
}
