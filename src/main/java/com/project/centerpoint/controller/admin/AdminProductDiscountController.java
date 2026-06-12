package com.project.centerpoint.controller.admin;

import com.project.centerpoint.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/product-discounts")
@RequiredArgsConstructor
public class AdminProductDiscountController {

    private final ProductService productService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-discounts/index";
    }

    @PostMapping("/update")
    public String updateDiscount(@RequestParam Long id, @RequestParam int discountPercent, RedirectAttributes redirectAttributes) {
        try {
            productService.updateProductDiscount(id, discountPercent);
            redirectAttributes.addFlashAttribute("success", "Cập nhật giảm giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/product-discounts";
    }
}
