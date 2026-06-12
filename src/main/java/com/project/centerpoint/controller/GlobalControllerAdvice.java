package com.project.centerpoint.controller;

import com.project.centerpoint.entity.Cart;
import com.project.centerpoint.entity.CartItem;
import com.project.centerpoint.entity.Category;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.service.CartService;
import com.project.centerpoint.service.CategoryService;
import com.project.centerpoint.service.CompareService;
import com.project.centerpoint.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryService categoryService;
    private final CartService cartService;
    private final UserService userService;
    private final CompareService compareService;

    @ModelAttribute("globalCategories")
    public List<Category> populateCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        User user = null;
        if (userDetails != null) {
            user = userService.getUserByEmail(userDetails.getUsername());
        }
        Cart cart = cartService.getCart(user, session.getId());
        if (cart != null && cart.getItems() != null) {
            return cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
        }
        return 0;
    }

    @ModelAttribute("compareItemCount")
    public int populateCompareItemCount(HttpSession session) {
        return compareService.getCompareCount(session);
    }
}
