package com.filahi.springboot.clothery.dto;


import com.filahi.springboot.clothery.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDTO {
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private Long quantity;
    private String imageUrl;
    private Long productId;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.name = orderItem.getName();
        this.unitPrice = orderItem.getUnitPrice();
        this.quantity = orderItem.getQuantity();
        this.imageUrl = orderItem.getImageUrl();

        if (orderItem.getProduct() != null) {
            this.productId = orderItem.getProduct().getId();
        }
    }
}
