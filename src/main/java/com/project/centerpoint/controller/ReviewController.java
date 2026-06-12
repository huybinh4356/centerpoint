package com.project.centerpoint.controller;

import com.project.centerpoint.entity.User;
import com.project.centerpoint.service.ReviewService;
import com.project.centerpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping("/submit")
    public String submitReview(
            @RequestParam Long orderId,
            @RequestParam Long orderItemId,
            @RequestParam int rating,
            @RequestParam String comment,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        if (userDetails == null) return "redirect:/login";
        User user = userService.getUserByEmail(userDetails.getUsername());
        
        try {
            reviewService.saveReview(user, orderItemId, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Cảm ơn bạn đã đánh giá sản phẩm!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/orders/" + orderId; 
    }

    @PostMapping("/update")
    public String updateReview(
            @RequestParam Long orderId,
            @RequestParam Long reviewId,
            @RequestParam int rating,
            @RequestParam String comment,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        if (userDetails == null) return "redirect:/login";
        User user = userService.getUserByEmail(userDetails.getUsername());
        
        try {
            reviewService.updateReview(user, reviewId, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Cập nhật đánh giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/orders/" + orderId;
    }
}
