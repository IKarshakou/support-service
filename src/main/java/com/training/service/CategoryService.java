package com.training.service;

import com.training.dto.category.CategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(UUID id);
}
