package com.ecommerce.application.service;

import com.ecommerce.application.entity.dtos.OrderRequestDto;
import com.ecommerce.application.entity.dtos.OrderResponseDto;
import com.ecommerce.application.entity.enums.OrderStatus;
import com.ecommerce.application.entity.enums.Role;
import java.util.List;

/**
 * Order Service Interface
 * Defines contracts for order-related business operations
 */
public interface IOrderService {
    OrderResponseDto createOrder(OrderRequestDto dto, Long userId, Role userRole);
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status, Role userRole);
    OrderResponseDto getOrderById(Long orderId, Long userId, Role userRole);
    List<OrderResponseDto> getUserOrders(Long userId, Role userRole);
    List<OrderResponseDto> getAllOrders(Role userRole);
}
