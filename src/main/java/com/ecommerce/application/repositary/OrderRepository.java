package com.ecommerce.application.repositary;

import com.ecommerce.application.entity.Order;
import com.ecommerce.application.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Order Repository
 * Handles database operations for Order entity
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
}
