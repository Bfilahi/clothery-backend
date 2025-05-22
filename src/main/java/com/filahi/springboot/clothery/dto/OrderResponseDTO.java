package com.filahi.springboot.clothery.dto;


import com.filahi.springboot.clothery.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private int totalQuantity;
    private BigDecimal totalPrice;
    private String orderTrackingNumber;
    private LocalDateTime dateCreated;
    private AddressDTO shippingAddress;
    private List<OrderItemDTO> orderItems;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.totalQuantity = order.getTotalQuantity();
        this.totalPrice = order.getTotalPrice();
        this.orderTrackingNumber = order.getOrderTrackingNumber();
        this.dateCreated = order.getDateCreated();

        if (order.getShippingAddress() != null) {
            this.shippingAddress = new AddressDTO(order.getShippingAddress());
        }

        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }
}
