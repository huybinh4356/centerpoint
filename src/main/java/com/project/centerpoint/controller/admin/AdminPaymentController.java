package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.Order;
import com.project.centerpoint.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final OrderService orderService;

    @GetMapping
    public String listPayments(Model model) {
        List<Order> onlineOrders = orderService.getAllOnlinePaymentOrders();
        model.addAttribute("orders", onlineOrders);
        return "admin/payments/index";
    }

    @PostMapping("/confirm/{id}")
    public String confirmPayment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        orderService.updatePaymentStatus(id, "PAID");
        redirectAttributes.addFlashAttribute("success", "Đã xác nhận thanh toán thành công cho đơn hàng #" + id);
        return "redirect:/admin/payments";
    }
}
