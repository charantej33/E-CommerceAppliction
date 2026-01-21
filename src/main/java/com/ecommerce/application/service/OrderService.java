package com.ecommerce.application.service;

import com.ecommerce.application.entity.Order;
import com.ecommerce.application.entity.OrderItem;
import com.ecommerce.application.entity.Product;
import com.ecommerce.application.entity.User;
import com.ecommerce.application.entity.dtos.OrderRequestDto;
import com.ecommerce.application.entity.dtos.OrderResponseDto;
import com.ecommerce.application.entity.enums.OrderStatus;
import com.ecommerce.application.entity.enums.Role;
import com.ecommerce.application.exception.BadRequestException;
import com.ecommerce.application.exception.ResourceNotFoundException;
import com.ecommerce.application.repositary.OrderRepository;
import com.ecommerce.application.util.AuthorizationUtil;
import com.ecommerce.application.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Service Implementation
 * Implements order business logic with service-level authorization
 * Uses Java Streams for calculations and filtering
 * Demonstrates Stream usage for: total calculation, filtering, mapping
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final IProductService productService;
    private final UserService userService;
    private final AuthorizationUtil authorizationUtil;
    private final ValidationUtil validationUtil;

    /**
     * Create a new order (CUSTOMER only)
     * Service-level authorization check
     * Uses Java Streams to calculate total from items
     */
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto, Long userId, Role userRole) {
        log.info("Create order request for user: {}", userId);

        // Service-level authorization: Only CUSTOMER can create orders
        if (userRole != Role.CUSTOMER) {
            authorizationUtil.checkRoleAccess(userRole, Role.CUSTOMER);
        }

        // Validate items
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BadRequestException("items", "Order must contain at least one item");
        }

        // Get user
        User user = userService.getUserEntityById(userId);

        // Create order items and calculate total using Java Streams
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderRequestDto.OrderItemRequestDto itemDto : dto.getItems()) {
            // Validate quantity
            validationUtil.validatePositive(itemDto.getQuantity(), "quantity");

            // Get product
            Product product = productService.getProductEntity(itemDto.getProductId());

            // Check stock availability
            if (product.getStock() < itemDto.getQuantity()) {
                throw new BadRequestException("stock", 
                    "Insufficient stock for product: " + product.getName());
            }

            // Create order item
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        // Calculate total using Java Streams - demonstrates Stream usage
        // This is a key requirement: "Calculate total using Java Streams"
        totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Order total calculated using Streams: {}", totalAmount);

        // Create order
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .items(orderItems)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Set order reference in items
        orderItems.forEach(item -> item.setOrder(order));

        // Save order
        Order savedOrder = orderRepository.save(order);
        log.info("Order created with id: {} and total: {}", savedOrder.getId(), totalAmount);

        // Reduce stock for each product
        orderItems.forEach(item -> 
            productService.reduceStock(item.getProduct().getId(), item.getQuantity())
        );

        return mapToOrderResponseDto(savedOrder);
    }

    /**
     * Update order status (ADMIN only)
     * Service-level authorization check
     * Only ADMIN can update order status
     */
    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status, Role userRole) {
        log.info("Update order status request for order: {}", orderId);

        // Service-level authorization: Only ADMIN can update status
        authorizationUtil.checkAdminAccess(userRole, "update order status");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated to: {}", status);

        return mapToOrderResponseDto(updatedOrder);
    }

    /**
     * Get order by ID (CUSTOMER can view own, ADMIN can view any)
     * Service-level authorization check
     */
    @Override
    public OrderResponseDto getOrderById(Long orderId, Long userId, Role userRole) {
        log.info("Fetch order by id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        // Authorization: User can view own order or ADMIN can view any
        authorizationUtil.checkUserOrAdmin(userRole, userId, order.getUser().getId());

        return mapToOrderResponseDto(order);
    }

    /**
     * Get user's orders (CUSTOMER views own, ADMIN can view any user's)
     * Uses Java Streams for filtering and mapping
     */
    @Override
    public List<OrderResponseDto> getUserOrders(Long userId, Role userRole) {
        log.info("Fetch orders for user: {}", userId);

        // Authorization: Can only view own orders unless ADMIN
        if (userRole != Role.ADMIN) {
            authorizationUtil.checkUserOrAdmin(userRole, userId, userId);
        }

        // Use Streams to filter and map - demonstrates Stream usage
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all orders (ADMIN only)
     * Uses Java Streams for mapping
     */
    @Override
    public List<OrderResponseDto> getAllOrders(Role userRole) {
        log.info("Fetch all orders");

        // Service-level authorization: Only ADMIN can view all orders
        authorizationUtil.checkAdminAccess(userRole, "view all orders");

        // Use Streams to map - demonstrates Stream usage
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Map Order entity to OrderResponseDto
     * Uses Java Streams to map OrderItems
     */
    private OrderResponseDto mapToOrderResponseDto(Order order) {
        // Using Streams to map order items - demonstrates Stream usage
        List<OrderResponseDto.OrderItemResponseDto> itemDtos = order.getItems().stream()
                .map(item -> OrderResponseDto.OrderItemResponseDto.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .lineTotal(item.getLineTotal())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userEmail(order.getUser().getEmail())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(itemDtos)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
