package com.training.service.impl;

import com.training.dto.category.CategoryDto;
import com.training.mapper.CategoryMapper;
import com.training.repository.CategoryRepository;
import com.training.service.CategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found.";
    private static final String CATEGORY_ALREADY_EXISTS_MSG = "Category with this name already exists.";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryMapper.convertListToDto(categoryRepository.findAll());
    }

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        var category = categoryMapper.convertToEntity(categoryDto);

        if (categoryRepository.exists(Example.of(category))) {
            throw new EntityExistsException(CATEGORY_ALREADY_EXISTS_MSG);
        }

        return categoryMapper.convertToDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        var category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND_MSG));
        categoryRepository.delete(category);
    }
}
