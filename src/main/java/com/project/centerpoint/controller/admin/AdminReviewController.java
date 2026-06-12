package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.Review;
import com.project.centerpoint.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewRepository reviewRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "admin/reviews/index";
    }

    @PostMapping("/reply")
    public String replyReview(@RequestParam Long reviewId, @RequestParam String reply, RedirectAttributes redirectAttributes) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setAdminReply(reply);
            reviewRepository.save(review);
            redirectAttributes.addFlashAttribute("success", "Đã gửi phản hồi thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đánh giá.");
        }
        return "redirect:/admin/reviews";
    }
}
