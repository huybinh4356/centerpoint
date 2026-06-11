package com.project.centerpoint.service;

import com.project.centerpoint.entity.*;
import com.project.centerpoint.repository.AddressRepository;
import com.project.centerpoint.repository.OrderRepository;
import com.project.centerpoint.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PromotionRepository promotionRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;

    @Transactional
    public Order createOrder(User user, Cart cart, String addressStr, String paymentMethod, String promoCode, String guestName, String guestPhone) {
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = new Address();
        if (user != null) {
            address.setUser(user);
        }
        
        String userName = (user != null && user.getFullName() != null) ? user.getFullName() : "";
        String userPhone = (user != null && user.getPhone() != null) ? user.getPhone() : "";
        
        String finalName = (guestName != null && !guestName.trim().isEmpty()) ? guestName : userName;
        String finalPhone = (guestPhone != null && !guestPhone.trim().isEmpty()) ? guestPhone : userPhone;
        
        address.setRecipientName(finalName);
        address.setPhone(finalPhone);
        address.setAddressLine(addressStr);
        address.setCity("");
        address.setDistrict("");
        address.setDefault(true);
        addressRepository.save(address);

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingFee(0);

        int totalAmount = cartService.getTotalPrice(cart);
        order.setTotalAmount(totalAmount);

        if (promoCode != null && !promoCode.isEmpty()) {
            Promotion promotion = promotionRepository.findByCodeAndIsActiveTrue(promoCode).orElse(null);
            if (promotion != null) {
                int discount = totalAmount * promotion.getDiscountPercent() / 100;
                order.setDiscountAmount(discount);
                order.setPromotion(promotion);
            } else {
                order.setDiscountAmount(0);
            }
        } else {
            order.setDiscountAmount(0);
        }

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            
            // Calculate final price with product discount
            int price = cartItem.getProduct().getPrice();
            int pDiscount = cartItem.getProduct().getDiscountPercent();
            int finalPrice = price - (price * pDiscount / 100);
            orderItem.setPrice(finalPrice);
            
            order.getItems().add(orderItem);
            
            // Reduce stock
            Product p = cartItem.getProduct();
            if (p.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product " + p.getName());
            }
            p.setStock(p.getStock() - cartItem.getQuantity());
        }

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart);

        return savedOrder;
    }

    public java.util.List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public java.util.List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void updatePaymentStatus(Long orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }
}
