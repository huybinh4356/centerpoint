package com.project.centerpoint.controller;

import com.project.centerpoint.entity.User;
import com.project.centerpoint.repository.UserRepository;
import com.project.centerpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "auth/register";
        }
        userService.registerUser(user);
        log.info("Gửi email kích hoạt (Mock) tới: {}", user.getEmail());
        return "redirect:/login?registered=true";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            log.info("Gửi link reset mật khẩu (Mock) tới: {} - Link: /reset-password?token={}", email, token);
        });
        model.addAttribute("message", "Nếu email tồn tại, một liên kết khôi phục đã được gửi!");
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        if (!userService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "auth/reset-password-error";
        }
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
        if (!userService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "auth/reset-password-error";
        }
        User user = userService.getUserByPasswordResetToken(token);
        if (user != null) {
            userService.changeUserPassword(user, password);
            return "redirect:/login?reset=true";
        }
        return "auth/reset-password-error";
    }
}
