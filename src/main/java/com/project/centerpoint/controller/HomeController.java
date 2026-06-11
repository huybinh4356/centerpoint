package com.project.centerpoint.controller;

import com.project.centerpoint.service.BannerService;
import com.project.centerpoint.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final BannerService bannerService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("latestProducts", productService.getLatestProducts());
        model.addAttribute("mainBanners", bannerService.getActiveMainBanners());
        model.addAttribute("subBanners", bannerService.getActiveSubBanners());
        return "index";
    }
}
