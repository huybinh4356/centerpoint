package com.project.centerpoint.repository;

import com.project.centerpoint.entity.Order;
import com.project.centerpoint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findAllByOrderByOrderDateDesc();

    @org.springframework.data.jpa.repository.Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status != 'CANCELLED'")
    Integer calculateTotalRevenue();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM Order o")
    Long countTotalOrders();
}
