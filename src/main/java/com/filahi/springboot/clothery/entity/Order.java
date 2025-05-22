package com.filahi.springboot.clothery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    @Column(name = "order_tracking_number")
    private String orderTrackingNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    @UpdateTimestamp
    private LocalDateTime dateUpdated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void add(OrderItem orderItem){
        if(orderItem != null){
            if(orderItems == null)
                orderItems = new HashSet<>();
            orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
    }

    @Override
    public String toString(){
        return "Order:{" +
                "totalQuantity=" + totalQuantity +
                ", totalPrice=" + totalPrice +
                ", stripeSessionId=" + stripeSessionId +
                ", orderTrackingNumber=" + orderTrackingNumber +
                ", shippingAddress=" + shippingAddress +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", customer=" + customer +
                "}";
    }
}
