package com.project.centerpoint.controller.admin;

import com.project.centerpoint.service.ProductService;
import com.project.centerpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/import")
@RequiredArgsConstructor
public class AdminImportController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    public String handleImport(@RequestParam("type") String type,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam(value = "redirectUrl", defaultValue = "/admin/dashboard") String redirectUrl,
                               RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file JSON để import.");
            return "redirect:" + redirectUrl;
        }

        try {
            if ("users".equals(type)) {
                userService.importUsersFromJson(file);
                redirectAttributes.addFlashAttribute("success", "Import dữ liệu Người dùng thành công!");
            } else if ("products".equals(type)) {
                productService.importProductsFromJson(file);
                redirectAttributes.addFlashAttribute("success", "Import dữ liệu Sản phẩm thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Loại dữ liệu import không hợp lệ.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi import: " + e.getMessage());
        }

        return "redirect:" + redirectUrl;
    }
}
