package com.ecommerce.application.service;

import com.ecommerce.application.entity.Category;
import com.ecommerce.application.entity.Product;
import com.ecommerce.application.entity.dtos.ProductRequestDto;
import com.ecommerce.application.entity.dtos.ProductResponseDto;
import com.ecommerce.application.entity.enums.Role;
import com.ecommerce.application.exception.BadRequestException;
import com.ecommerce.application.exception.ResourceNotFoundException;
import com.ecommerce.application.repositary.ProductRepository;
import com.ecommerce.application.util.AuthorizationUtil;
import com.ecommerce.application.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Service Implementation
 * Implements product business logic with service-level authorization
 * Uses streams for filtering and mapping
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final ValidationUtil validationUtil;
    private final AuthorizationUtil authorizationUtil;

    /**
     * Create a new product (ADMIN only)
     * Service-level authorization check
     */
    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, Role userRole) {
        log.info("Create product request: {}", dto.getName());

        // Service-level authorization: Only ADMIN can create
        authorizationUtil.checkAdminAccess(userRole, "create product");

        // Validate input
        validationUtil.validateNotEmpty(dto.getName(), "name");
        validationUtil.validatePrice(dto.getPrice());
        validationUtil.validateStock(dto.getStock());

        // Verify category exists
        Category category = categoryService.getCategoryEntity(dto.getCategoryId());

        // Create and save product
        Product product = Product.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription() != null ? dto.getDescription().trim() : null)
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created with id: {}", savedProduct.getId());

        return mapToProductResponseDto(savedProduct);
    }

    /**
     * Update an existing product (ADMIN only)
     * Service-level authorization check
     */
    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto, Role userRole) {
        log.info("Update product request for id: {}", id);

        // Service-level authorization: Only ADMIN can update
        authorizationUtil.checkAdminAccess(userRole, "update product");

        // Validate input
        validationUtil.validateNotEmpty(dto.getName(), "name");
        validationUtil.validatePrice(dto.getPrice());
        validationUtil.validateStock(dto.getStock());

        // Check if product exists
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        // Verify category exists if changed
        Category category = categoryService.getCategoryEntity(dto.getCategoryId());

        // Update product
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated with id: {}", updatedProduct.getId());

        return mapToProductResponseDto(updatedProduct);
    }

    /**
     * Delete a product (ADMIN only)
     * Service-level authorization check
     */
    @Override
    public void deleteProduct(Long id, Role userRole) {
        log.info("Delete product request for id: {}", id);

        // Service-level authorization: Only ADMIN can delete
        authorizationUtil.checkAdminAccess(userRole, "delete product");

        // Check if product exists
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }

        productRepository.deleteById(id);
        log.info("Product deleted with id: {}", id);
    }

    /**
     * Get product by ID (Public)
     */
    @Override
    public ProductResponseDto getProductById(Long id) {
        log.info("Fetching product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        return mapToProductResponseDto(product);
    }

    /**
     * Get all products (Public)
     * Uses Java Streams for filtering
     */
    @Override
    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products");

        return productRepository.findAll().stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get products by category (Public)
     * Uses Java Streams for filtering
     */
    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        log.info("Fetching products for category: {}", categoryId);

        // Verify category exists
        categoryService.getCategoryEntity(categoryId);

        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get product entity (internal use)
     */
    @Override
    public Product getProductEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    /**
     * Reduce product stock after order
     */
    @Override
    public void reduceStock(Long productId, Integer quantity) {
        log.info("Reducing stock for product: {} by quantity: {}", productId, quantity);

        Product product = getProductEntity(productId);
        
        if (product.getStock() < quantity) {
            throw new BadRequestException("stock", "Insufficient stock available");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        log.info("Stock reduced. New stock: {}", product.getStock());
    }

    /**
     * Map Product entity to ProductResponseDto
     */
    private ProductResponseDto mapToProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
