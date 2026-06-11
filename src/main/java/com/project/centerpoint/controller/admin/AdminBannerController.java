package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.Banner;
import com.project.centerpoint.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("banners", bannerService.getAllBanners());
        return "admin/banners/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("banner", new Banner());
        return "admin/banners/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("banner") Banner banner,
                       @RequestParam("imageFile") MultipartFile file,
                       RedirectAttributes redirectAttributes) {
        try {
            bannerService.saveBanner(banner, file);
            redirectAttributes.addFlashAttribute("success", "Lưu banner thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh: " + e.getMessage());
        }
        return "redirect:/admin/banners";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Banner banner = bannerService.getBannerById(id);
        if (banner != null) {
            model.addAttribute("banner", banner);
            return "admin/banners/form";
        }
        return "redirect:/admin/banners";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bannerService.deleteBanner(id);
        redirectAttributes.addFlashAttribute("success", "Xóa banner thành công!");
        return "redirect:/admin/banners";
    }
}
