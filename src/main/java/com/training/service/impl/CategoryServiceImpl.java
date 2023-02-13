package com.training.service.impl;

import com.training.dto.category.CategoryDto;
import com.training.mapper.CategoryMapper;
import com.training.repository.CategoryRepository;
import com.training.service.CategoryService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found.";
    private static final String CATEGORY_ALREADY_EXISTS_MSG = "Category with this name already exists.";

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryMapper.convertListToDto(categoryRepository.findAll());
    }

    @Override
//    @PostAuthorize("returnObject.name != 'some thing'")
//    @Transactional(noRollbackFor = IllegalArgumentException.class)
//    @Transactional(rollbackFor = IllegalArgumentException.class)
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        var category = categoryMapper.convertToEntity(categoryDto);

        if (categoryRepository.exists(Example.of(category))) {
            throw new EntityExistsException(CATEGORY_ALREADY_EXISTS_MSG);
        }

        var savedCategory = categoryRepository.save(category);

//        if (true) {
//            throw new IllegalArgumentException();
//        }

        return categoryMapper.convertToDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        categoryRepository.deleteCategoryById(categoryId);
    }
}
