package com.ecommerce.application.service;

import com.ecommerce.application.entity.Category;
import com.ecommerce.application.entity.dtos.CategoryRequestDto;
import com.ecommerce.application.entity.dtos.CategoryResponseDto;
import com.ecommerce.application.entity.enums.Role;
import com.ecommerce.application.exception.BadRequestException;
import com.ecommerce.application.exception.ResourceNotFoundException;
import com.ecommerce.application.repositary.CategoryRepository;
import com.ecommerce.application.util.AuthorizationUtil;
import com.ecommerce.application.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Category Service Implementation
 * Implements category business logic with service-level authorization
 * Follows SOLID principles and design patterns
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;
    private final AuthorizationUtil authorizationUtil;

    /**
     * Create a new category (ADMIN only)
     * Service-level authorization check
     */
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto dto, Role userRole) {
        log.info("Create category request: {}", dto.getName());

        // Service-level authorization: Only ADMIN can create
        authorizationUtil.checkAdminAccess(userRole, "create category");

        // Validate input
        validationUtil.validateNotEmpty(dto.getName(), "name");
        if (categoryRepository.existsByName(dto.getName().trim())) {
            throw new BadRequestException("name", "Category name already exists");
        }

        // Create and save category
        Category category = Category.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription() != null ? dto.getDescription().trim() : null)
                .build();

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created with id: {}", savedCategory.getId());

        return mapToCategoryResponseDto(savedCategory);
    }

    /**
     * Update an existing category (ADMIN only)
     * Service-level authorization check
     */
    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto, Role userRole) {
        log.info("Update category request for id: {}", id);

        // Service-level authorization: Only ADMIN can update
        authorizationUtil.checkAdminAccess(userRole, "update category");

        // Validate input
        validationUtil.validateNotEmpty(dto.getName(), "name");

        // Check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        // Check if new name is already taken by another category
        if (!category.getName().equalsIgnoreCase(dto.getName()) 
                && categoryRepository.existsByName(dto.getName().trim())) {
            throw new BadRequestException("name", "Category name already exists");
        }

        // Update category
        category.setName(dto.getName().trim());
        category.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated with id: {}", updatedCategory.getId());

        return mapToCategoryResponseDto(updatedCategory);
    }

    /**
     * Delete a category (ADMIN only)
     * Service-level authorization check
     */
    @Override
    public void deleteCategory(Long id, Role userRole) {
        log.info("Delete category request for id: {}", id);

        // Service-level authorization: Only ADMIN can delete
        authorizationUtil.checkAdminAccess(userRole, "delete category");

        // Check if category exists
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }

        categoryRepository.deleteById(id);
        log.info("Category deleted with id: {}", id);
    }

    /**
     * Get category by ID (Public)
     */
    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        log.info("Fetching category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        return mapToCategoryResponseDto(category);
    }

    /**
     * Get all categories (Public)
     */
    @Override
    public List<CategoryResponseDto> getAllCategories() {
        log.info("Fetching all categories");

        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get category entity (internal use)
     */
    @Override
    public Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    /**
     * Map Category entity to CategoryResponseDto
     */
    private CategoryResponseDto mapToCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
