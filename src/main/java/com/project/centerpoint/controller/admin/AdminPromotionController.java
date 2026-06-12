package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.Promotion;
import com.project.centerpoint.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class AdminPromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("promotions", promotionService.getAllPromotions());
        return "admin/promotions/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "admin/promotions/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("promotion") Promotion promotion, RedirectAttributes redirectAttributes) {
        try {
            promotionService.savePromotion(promotion);
            redirectAttributes.addFlashAttribute("success", "Lưu mã giảm giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            model.addAttribute("promotion", promotion);
            return "admin/promotions/form";
        }
        return "redirect:/admin/promotions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("success", "Xóa mã giảm giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/promotions";
    }
}
