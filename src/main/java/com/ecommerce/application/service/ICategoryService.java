package com.ecommerce.application.service;

import com.ecommerce.application.entity.Category;
import com.ecommerce.application.entity.dtos.CategoryRequestDto;
import com.ecommerce.application.entity.dtos.CategoryResponseDto;
import com.ecommerce.application.entity.enums.Role;
import java.util.List;

/**
 * Category Service Interface
 * Defines contracts for category-related business operations
 */
public interface ICategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto dto, Role userRole);
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto, Role userRole);
    void deleteCategory(Long id, Role userRole);
    CategoryResponseDto getCategoryById(Long id);
    List<CategoryResponseDto> getAllCategories();
    Category getCategoryEntity(Long id);
}
