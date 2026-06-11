package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.Brand;
import com.project.centerpoint.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/brands/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "admin/brands/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("brand") Brand brand) {
        brandService.saveBrand(brand);
        return "redirect:/admin/brands";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.getBrandById(id);
        if (brand != null) {
            model.addAttribute("brand", brand);
            return "admin/brands/form";
        }
        return "redirect:/admin/brands";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/brands";
    }
}
