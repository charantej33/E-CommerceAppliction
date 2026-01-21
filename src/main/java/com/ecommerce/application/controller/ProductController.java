package com.ecommerce.application.controller;

import com.ecommerce.application.entity.User;
import com.ecommerce.application.entity.dtos.ProductRequestDto;
import com.ecommerce.application.entity.dtos.ProductResponseDto;
import com.ecommerce.application.service.ProductService;
import com.ecommerce.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Controller
 * Handles product-related HTTP requests
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    /**
     * Create a new product (ADMIN only)
     * POST /api/products
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto dto) {
        log.info("Create product request");
        User currentUser = getCurrentUser();
        return productService.createProduct(dto, currentUser.getRole());
    }

    /**
     * Update an existing product (ADMIN only)
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDto dto) {
        log.info("Update product request for id: {}", id);
        User currentUser = getCurrentUser();
        return productService.updateProduct(id, dto, currentUser.getRole());
    }

    /**
     * Delete a product (ADMIN only)
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        log.info("Delete product request for id: {}", id);
        User currentUser = getCurrentUser();
        productService.deleteProduct(id, currentUser.getRole());
    }

    /**
     * Get product by ID (Public)
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        log.info("Fetch product by id: {}", id);
        return productService.getProductById(id);
    }

    /**
     * Get all products (Public)
     * GET /api/products
     */
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetch all products");
        return productService.getAllProducts();
    }

    /**
     * Get products by category (Public)
     * GET /api/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> getProductsByCategory(@PathVariable Long categoryId) {
        log.info("Fetch products by category: {}", categoryId);
        return productService.getProductsByCategory(categoryId);
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
