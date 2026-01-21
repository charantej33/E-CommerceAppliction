package com.ecommerce.application.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category Request DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryRequestDto {
    private String name;
    private String description;
}
