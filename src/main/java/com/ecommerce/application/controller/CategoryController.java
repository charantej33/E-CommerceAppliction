package com.ecommerce.application.controller;

import com.ecommerce.application.entity.User;
import com.ecommerce.application.entity.dtos.CategoryRequestDto;
import com.ecommerce.application.entity.dtos.CategoryResponseDto;
import com.ecommerce.application.service.CategoryService;
import com.ecommerce.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Controller
 * Handles category-related HTTP requests
 */
@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * Create a new category (ADMIN only)
     * POST /api/categories
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody CategoryRequestDto dto) {
        log.info("Create category request");
        User currentUser = getCurrentUser();
        return categoryService.createCategory(dto, currentUser.getRole());
    }

    /**
     * Update an existing category (ADMIN only)
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDto dto) {
        log.info("Update category request for id: {}", id);
        User currentUser = getCurrentUser();
        return categoryService.updateCategory(id, dto, currentUser.getRole());
    }

    /**
     * Delete a category (ADMIN only)
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        log.info("Delete category request for id: {}", id);
        User currentUser = getCurrentUser();
        categoryService.deleteCategory(id, currentUser.getRole());
    }

    /**
     * Get category by ID (Public)
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        log.info("Fetch category by id: {}", id);
        return categoryService.getCategoryById(id);
    }

    /**
     * Get all categories (Public)
     * GET /api/categories
     */
    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        log.info("Fetch all categories");
        return categoryService.getAllCategories();
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
