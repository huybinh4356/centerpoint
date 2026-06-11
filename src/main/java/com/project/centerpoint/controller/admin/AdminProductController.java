package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.Product;
import com.project.centerpoint.service.BrandService;
import com.project.centerpoint.service.CategoryService;
import com.project.centerpoint.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("product") Product product,
                       @RequestParam("imageFiles") MultipartFile[] files,
                       @RequestParam(value = "primaryImageIndex", defaultValue = "0") int primaryImageIndex) throws IOException {
        productService.saveProduct(product, files, primaryImageIndex);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            return "admin/products/form";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/import")
    public String importJson(@RequestParam(value = "file", required = false) MultipartFile file, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file JSON để import!");
            return "redirect:/admin/products";
        }
        try {
            productService.importProductsFromJson(file);
            redirectAttributes.addFlashAttribute("success", "Import sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/import")
    public String getImportRedirect() {
        return "redirect:/admin/products";
    }

}
