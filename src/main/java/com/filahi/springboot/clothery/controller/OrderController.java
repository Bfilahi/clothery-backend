package com.filahi.springboot.clothery.controller;

import com.filahi.springboot.clothery.dto.OrderResponseDTO;
import com.filahi.springboot.clothery.entity.Customer;
import com.filahi.springboot.clothery.entity.Order;
import com.filahi.springboot.clothery.repository.CustomerRepository;
import com.filahi.springboot.clothery.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }


    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(@RequestParam String auth0Id) {

        // Find customer by Auth0 ID
        Customer customer = customerRepository.findByAuth0Id(auth0Id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        // Get all orders for the customer
        List<Order> orders = orderRepository.findByCustomerId(customer.getId());

        // Convert to DTOs
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long orderId,
            @RequestParam String auth0Id) {

        // Find customer by Auth0 ID
        Customer customer = customerRepository.findByAuth0Id(auth0Id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        // Find the order by ID and check if it belongs to the customer
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Order order = orderOpt.get();

        if(!(order.getCustomer().getId() == customer.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(new OrderResponseDTO(order));
    }
}
