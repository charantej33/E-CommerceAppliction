package com.ecommerce.application.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Product Request DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
}
