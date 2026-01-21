package com.ecommerce.application.entity.dtos;

import com.ecommerce.application.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order Response DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemResponseDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class OrderItemResponseDto {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal lineTotal;
    }
}
