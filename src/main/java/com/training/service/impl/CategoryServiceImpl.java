package com.training.service.impl;

import com.training.dto.category.CategoryDto;
import com.training.entity.Category;
import com.training.mapper.CategoryMapper;
import com.training.repository.CategoryRepository;
import com.training.service.CategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found.";
    private static final String CATEGORY_ALREADY_EXISTS_MSG = "Category with this name already exists.";

    @Override
    public List<CategoryDto> findAll() {
        return categoryMapper.convertListToDto(categoryRepository.findAll());
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.convertToEntity(categoryDto);

        if (categoryRepository.exists(Example.of(category))) {
            throw new EntityExistsException(CATEGORY_ALREADY_EXISTS_MSG);
        }

        return categoryMapper.convertToDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND_MSG));
        categoryRepository.delete(category);
    }
}
