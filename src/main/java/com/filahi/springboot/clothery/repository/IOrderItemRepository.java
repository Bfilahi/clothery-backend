package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
}
