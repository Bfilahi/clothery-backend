package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);

    // Find a specific order based on the Stripe session ID
    Order findByStripeSessionId(String sessionId);
}
