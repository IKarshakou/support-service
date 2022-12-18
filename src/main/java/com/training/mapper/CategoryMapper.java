package com.training.mapper;

import com.training.dto.category.CategoryDto;
import com.training.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryDto convertToDto(Category category);

    Category convertToEntity(CategoryDto categoryDto);

    List<CategoryDto> convertListToDto(List<Category> categories);

    List<Category> convertListToEntity(List<CategoryDto> categoriesDto);
}
