package com.ecommerce.application.service;

import com.ecommerce.application.entity.Product;
import com.ecommerce.application.entity.dtos.ProductRequestDto;
import com.ecommerce.application.entity.dtos.ProductResponseDto;
import com.ecommerce.application.entity.enums.Role;
import java.util.List;

/**
 * Product Service Interface
 * Defines contracts for product-related business operations
 */
public interface IProductService {
    ProductResponseDto createProduct(ProductRequestDto dto, Role userRole);
    ProductResponseDto updateProduct(Long id, ProductRequestDto dto, Role userRole);
    void deleteProduct(Long id, Role userRole);
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getAllProducts();
    List<ProductResponseDto> getProductsByCategory(Long categoryId);
    Product getProductEntity(Long id);
    void reduceStock(Long productId, Integer quantity);
}
