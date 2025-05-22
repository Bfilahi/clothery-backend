package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.entity.Address;
import com.filahi.springboot.clothery.entity.Order;
import com.filahi.springboot.clothery.repository.IOrderRepository;
import com.filahi.springboot.clothery.service.IOrderService;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(IOrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public void saveShippingAddress(String sessionId, Address address) {

        Order order = this.orderRepository.findByStripeSessionId(sessionId);
        if(order == null)
            throw new NoResultException("Order not found with session ID: " + sessionId);

        order.setShippingAddress(address);
        this.orderRepository.save(order);
    }
}
