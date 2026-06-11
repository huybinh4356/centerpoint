package com.project.centerpoint.service;

import com.project.centerpoint.entity.Cart;
import com.project.centerpoint.entity.CartItem;
import com.project.centerpoint.entity.Product;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.repository.CartItemRepository;
import com.project.centerpoint.repository.CartRepository;
import com.project.centerpoint.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Cart getCart(User user, String sessionId) {
        if (user != null) {
            Cart userCart = cartRepository.findByUser(user).orElseGet(() -> {
                Cart cart = new Cart();
                cart.setUser(user);
                return cartRepository.save(cart);
            });
            
            Optional<Cart> sessionCartOpt = cartRepository.findBySessionId(sessionId);
            if (sessionCartOpt.isPresent()) {
                Cart sessionCart = sessionCartOpt.get();
                if (!sessionCart.getItems().isEmpty()) {
                    for (CartItem item : sessionCart.getItems()) {
                        Optional<CartItem> existingItem = userCart.getItems().stream()
                                .filter(i -> i.getProduct().getId().equals(item.getProduct().getId()))
                                .findFirst();
                        if (existingItem.isPresent()) {
                            existingItem.get().setQuantity(existingItem.get().getQuantity() + item.getQuantity());
                            cartItemRepository.save(existingItem.get());
                        } else {
                            item.setCart(userCart);
                            userCart.getItems().add(item);
                            cartItemRepository.save(item);
                        }
                    }
                    sessionCart.getItems().clear();
                }
                cartRepository.delete(sessionCart);
            }
            return userCart;
        } else {
            return cartRepository.findBySessionId(sessionId).orElseGet(() -> {
                Cart cart = new Cart();
                cart.setSessionId(sessionId);
                return cartRepository.save(cart);
            });
        }
    }

    @Transactional
    public Cart addItem(User user, String sessionId, Long productId, int quantity) {
        Cart cart = getCart(user, sessionId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        
        return cartRepository.save(cart);
    }

    @Transactional
    public void updateItemQuantity(User user, String sessionId, Long itemId, int quantity) {
        Cart cart = getCart(user, sessionId);
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Unauthorized access to cart item");
        }
        if (quantity > 0) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
    }

    @Transactional
    public void removeItem(User user, String sessionId, Long itemId) {
        Cart cart = getCart(user, sessionId);
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Unauthorized access to cart item");
        }
        cartItemRepository.delete(item);
    }
    
    @Transactional
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    public int getTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToInt(item -> {
                    int price = item.getProduct().getPrice();
                    int discount = item.getProduct().getDiscountPercent();
                    int finalPrice = price - (price * discount / 100);
                    return finalPrice * item.getQuantity();
                })
                .sum();
    }
}
