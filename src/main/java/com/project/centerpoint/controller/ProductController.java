package com.project.centerpoint.controller;

import com.project.centerpoint.service.BrandService;
import com.project.centerpoint.service.CategoryService;
import com.project.centerpoint.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) com.project.centerpoint.entity.ProductType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "newest") String sort,
            Model model) {

        model.addAttribute("productPage", productService.searchProducts(keyword, categoryId, brandId, type, page, size, sort));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("type", type);
        model.addAttribute("sort", sort);

        return "products/index";
    }

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        var product = productService.getProductBySlug(slug);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/detail";
    }
}
