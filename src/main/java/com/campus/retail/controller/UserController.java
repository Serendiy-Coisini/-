package com.campus.retail.controller;

import com.campus.retail.entity.User;
import com.campus.retail.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

/**
 * 用户 Controller - 登录、注册
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam("studentId") String studentId,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        User user = userService.getByStudentId(studentId);
        if (user == null || !user.getPassword().equals(password)) {
            ra.addFlashAttribute("msg", "学号或密码错误");
            return "redirect:/login";
        }
        session.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("studentId") String studentId,
                           @RequestParam String password,
                           @RequestParam(required = false) String confirmPassword,
                           @RequestParam String dormitoryArea,
                           @RequestParam String dormitoryBuilding,
                           @RequestParam String dormitoryNumber,
                           RedirectAttributes ra) {
        // 验证学号
        if (studentId == null || studentId.isBlank()) {
            ra.addFlashAttribute("msg", "学号不能为空");
            return "redirect:/register";
        }
        studentId = studentId.trim();
        if (studentId.length() != 12 || !studentId.matches("\\d{12}")) {
            ra.addFlashAttribute("msg", "学号必须为12位数字");
            return "redirect:/register";
        }
        
        // 验证密码
        if (password == null || password.length() < 6) {
            ra.addFlashAttribute("msg", "密码至少6位");
            return "redirect:/register";
        }
        if (confirmPassword != null && !password.equals(confirmPassword)) {
            ra.addFlashAttribute("msg", "两次密码不一致");
            return "redirect:/register";
        }
        
        // 验证宿舍信息
        if (dormitoryArea == null || dormitoryArea.isBlank()) {
            ra.addFlashAttribute("msg", "请选择宿舍区域");
            return "redirect:/register";
        }
        if (dormitoryBuilding == null || dormitoryBuilding.isBlank()) {
            ra.addFlashAttribute("msg", "请选择栋号");
            return "redirect:/register";
        }
        if (dormitoryNumber == null || dormitoryNumber.isBlank()) {
            ra.addFlashAttribute("msg", "请填写宿舍号");
            return "redirect:/register";
        }
        
        // 组合宿舍信息：区域+栋号+宿舍号，如"南一区A栋301"
        String dormitory = dormitoryArea.trim() + dormitoryBuilding.trim() + "栋" + dormitoryNumber.trim();
        
        try {
            userService.register(studentId, password, dormitory);
            ra.addFlashAttribute("msg", "注册成功，请登录");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("msg", e.getMessage());
            return "redirect:/register";
        }
    }
}
