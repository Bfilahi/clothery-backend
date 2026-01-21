package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);

    // Find a specific order based on the Stripe session ID
    Order findByStripeSessionId(String sessionId);
}
