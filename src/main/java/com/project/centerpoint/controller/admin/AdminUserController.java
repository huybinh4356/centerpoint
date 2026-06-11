package com.project.centerpoint.controller.admin;

import com.project.centerpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.entity.Role;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users/index";
    }

    @GetMapping("/lock/{id}")
    public String lockUser(@PathVariable Long id) {
        userService.updateUserStatus(id, true);
        return "redirect:/admin/users";
    }

    @GetMapping("/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        userService.updateUserStatus(id, false);
        return "redirect:/admin/users";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/form";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userService.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/admin/users/create";
        }
        user.setRole(Role.ROLE_USER); // default
        userService.registerUser(user);
        redirectAttributes.addFlashAttribute("success", "Thêm người dùng thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{email}")
    public String editUserForm(@PathVariable String email, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/users/form";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setPhone(user.getPhone());
            existingUser.setRole(user.getRole());
            userService.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("success", "Cập nhật người dùng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
        }
        return "redirect:/admin/users";
    }
}
