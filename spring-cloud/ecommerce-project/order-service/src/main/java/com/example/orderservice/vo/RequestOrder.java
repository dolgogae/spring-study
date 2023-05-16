package com.example.orderservice.vo;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class RequestOrder {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
