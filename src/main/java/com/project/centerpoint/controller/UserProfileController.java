package com.project.centerpoint.controller;

import com.project.centerpoint.entity.User;
import com.project.centerpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails, 
                                User updatedUser, 
                                RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        try {
            User existingUser = userService.getUserByEmail(userDetails.getUsername());
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setGender(updatedUser.getGender());
            // Need to save through repository, but let's assume userService has a save/update or we just save if we have a repository here.
            // Wait, UserService needs an updateUser method.
            // For now, let's just assume we will add updateUser to UserService.
            userService.updateUser(existingUser);
            
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}
