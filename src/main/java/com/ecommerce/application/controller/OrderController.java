package com.ecommerce.application.controller;

import com.ecommerce.application.entity.User;
import com.ecommerce.application.entity.dtos.OrderRequestDto;
import com.ecommerce.application.entity.dtos.OrderResponseDto;
import com.ecommerce.application.entity.enums.OrderStatus;
import com.ecommerce.application.service.OrderService;
import com.ecommerce.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order Controller
 * Handles order-related HTTP requests
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    /**
     * Create a new order (CUSTOMER only)
     * POST /api/orders
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto dto) {
        log.info("Create order request");
        User currentUser = getCurrentUser();
        return orderService.createOrder(dto, currentUser.getId(), currentUser.getRole());
    }

    /**
     * Update order status (ADMIN only)
     * PATCH /api/orders/{id}/status
     */
    @PatchMapping("/{id}/status")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        log.info("Update order status request for id: {}", id);
        User currentUser = getCurrentUser();
        return orderService.updateOrderStatus(id, status, currentUser.getRole());
    }

    /**
     * Get order by ID
     * Authorization: User can view own order, ADMIN can view any
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        log.info("Fetch order by id: {}", id);
        User currentUser = getCurrentUser();
        return orderService.getOrderById(id, currentUser.getId(), currentUser.getRole());
    }

    /**
     * Get current user's orders
     * Authorization: User views own orders, ADMIN can query any user's
     * GET /api/orders/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<OrderResponseDto> getUserOrders(@PathVariable Long userId) {
        log.info("Fetch orders for user: {}", userId);
        User currentUser = getCurrentUser();
        return orderService.getUserOrders(userId, currentUser.getRole());
    }

    /**
     * Get current user's orders (my orders)
     * GET /api/orders/my/orders
     */
    @GetMapping("/my/orders")
    public List<OrderResponseDto> getMyOrders() {
        log.info("Fetch my orders");
        User currentUser = getCurrentUser();
        return orderService.getUserOrders(currentUser.getId(), currentUser.getRole());
    }

    /**
     * Get all orders (ADMIN only)
     * GET /api/orders/all
     */
    @GetMapping("/all")
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetch all orders");
        User currentUser = getCurrentUser();
        return orderService.getAllOrders(currentUser.getRole());
    }

    /**
     * Helper method to get current authenticated user
     */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        return userService.getUserByEmail(email);
    }
}
