package com.ecommerce.application.entity.dtos;

import com.ecommerce.application.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * Order Request DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDto {
    private List<OrderItemRequestDto> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class OrderItemRequestDto {
        private Long productId;
        private Integer quantity;
    }
}
