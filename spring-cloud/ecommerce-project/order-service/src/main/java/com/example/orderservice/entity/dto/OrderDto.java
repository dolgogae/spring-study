package com.example.orderservice.entity.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data @ToString
public class OrderDto implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
