package com.project.centerpoint.controller;

import com.project.centerpoint.entity.Cart;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.service.CartService;
import com.project.centerpoint.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails != null) {
            return userService.getUserByEmail(userDetails.getUsername());
        }
        return null;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, HttpSession session, Model model) {
        User user = getCurrentUser(userDetails);
        Cart cart = cartService.getCart(user, session.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.getTotalPrice(cart));
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(userDetails);
        cartService.addItem(user, session.getId(), productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long itemId,
            @RequestParam int quantity,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {
        User user = getCurrentUser(userDetails);
        cartService.updateItemQuantity(user, session.getId(), itemId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {
        User user = getCurrentUser(userDetails);
        cartService.removeItem(user, session.getId(), itemId);
        return "redirect:/cart";
    }
}
