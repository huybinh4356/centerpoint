package com.project.centerpoint.controller.admin;

import com.project.centerpoint.entity.OrderStatus;
import com.project.centerpoint.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders/index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        var order = orderService.getOrderById(id);
        if (order == null) return "redirect:/admin/orders";
        model.addAttribute("order", order);
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/{id}/payment-status")
    public String updatePaymentStatus(@PathVariable Long id, @RequestParam String paymentStatus) {
        orderService.updatePaymentStatus(id, paymentStatus);
        return "redirect:/admin/orders/" + id;
    }
}
