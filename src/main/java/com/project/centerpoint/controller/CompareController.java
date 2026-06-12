package com.project.centerpoint.controller;

import com.project.centerpoint.entity.Product;
import com.project.centerpoint.service.CompareService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/compare")
@RequiredArgsConstructor
public class CompareController {

    private final CompareService compareService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        List<Product> compareProducts = compareService.getCompareProducts(session);
        model.addAttribute("compareProducts", compareProducts);
        return "compare/index";
    }

    @PostMapping("/add")
    public String addToCompare(@RequestParam Long productId,
                               HttpSession session,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        boolean added = compareService.addProductToCompare(productId, session);
        if (added) {
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào danh sách so sánh.");
        } else {
            if (compareService.getCompareCount(session) >= 3) {
                redirectAttributes.addFlashAttribute("error", "Danh sách so sánh đã đầy (tối đa 3 sản phẩm).");
            } else {
                redirectAttributes.addFlashAttribute("info", "Sản phẩm đã có trong danh sách so sánh.");
            }
        }
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/products");
    }

    @GetMapping("/remove/{productId}")
    public String remove(@PathVariable Long productId, HttpSession session, HttpServletRequest request) {
        compareService.removeProductFromCompare(productId, session);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/compare");
    }

    @GetMapping("/clear")
    public String clear(HttpSession session, HttpServletRequest request) {
        compareService.clearCompare(session);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/compare");
    }
}
