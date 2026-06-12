package com.project.centerpoint.controller;

import com.project.centerpoint.entity.User;
import com.project.centerpoint.service.OrderService;
import com.project.centerpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("orders", orderService.getUserOrders(user));
        return "orders/index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User user = userService.getUserByEmail(userDetails.getUsername());
        var order = orderService.getOrderById(id);
        
        if (order == null || order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }
        
        model.addAttribute("order", order);
        return "orders/detail";
    }
}
