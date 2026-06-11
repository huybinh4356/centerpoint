package com.project.centerpoint.controller;

import com.project.centerpoint.entity.Cart;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.service.CartService;
import com.project.centerpoint.service.OrderService;
import com.project.centerpoint.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String checkoutPage(@AuthenticationPrincipal UserDetails userDetails, HttpSession session, Model model) {
        User user = null;
        if (userDetails != null) {
            user = userService.getUserByEmail(userDetails.getUsername());
        }
        Cart cart = cartService.getCart(user, session.getId());
        
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.getTotalPrice(cart));
        model.addAttribute("user", user);
        return "checkout/index";
    }

    @PostMapping
    public String processCheckout(
            @RequestParam String address,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) String promoCode,
            @RequestParam(required = false) String guestName,
            @RequestParam(required = false) String guestPhone,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User user = null;
        if (userDetails != null) {
            user = userService.getUserByEmail(userDetails.getUsername());
        }
        Cart cart = cartService.getCart(user, session.getId());

        try {
            var order = orderService.createOrder(user, cart, address, paymentMethod, promoCode, guestName, guestPhone);
            redirectAttributes.addFlashAttribute("paymentMethod", order.getPaymentMethod());
            redirectAttributes.addFlashAttribute("orderId", order.getId());
            return "redirect:/checkout/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
    
    @GetMapping("/success")
    public String successPage() {
        return "checkout/success";
    }
}
