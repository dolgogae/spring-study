package com.example.orderservice.repository;

import com.example.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByProductId(String orderId);
    Iterable<OrderEntity> findByUserId(String userId);
    OrderEntity findByOrderId(String orderId);
}
