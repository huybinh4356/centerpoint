package com.project.centerpoint.controller.admin;

import com.project.centerpoint.repository.OrderRepository;
import com.project.centerpoint.repository.ProductRepository;
import com.project.centerpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        Integer revenue = orderRepository.calculateTotalRevenue();
        model.addAttribute("totalRevenue", revenue != null ? revenue : 0);
        model.addAttribute("totalOrders", orderRepository.countTotalOrders());
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        
        model.addAttribute("recentOrders", orderRepository.findAllByOrderByOrderDateDesc().stream().limit(5).toList());
        return "admin/dashboard";
    }
}
