package com.project.centerpoint.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin/settings/payment")
public class AdminPaymentSettingController {

    private static final String SYSTEM_UPLOAD_DIR = "uploads/system";
    private static final String QR_CODE_FILENAME = "qr-code.png";

    @GetMapping
    public String paymentSettings() {
        return "admin/settings/payment";
    }

    @PostMapping("/upload-qr")
    public String uploadQrCode(@RequestParam("qrImage") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file ảnh để tải lên.");
            return "redirect:/admin/settings/payment";
        }

        try {
            Path uploadPath = Paths.get(SYSTEM_UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(QR_CODE_FILENAME);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            redirectAttributes.addFlashAttribute("success", "Cập nhật mã QR thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu file.");
        }

        return "redirect:/admin/settings/payment";
    }
}
