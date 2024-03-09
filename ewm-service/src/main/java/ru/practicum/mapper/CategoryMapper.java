package ru.practicum.mapper;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto, Long id) {
        Category category = new Category(
                id,
                categoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getName());
        return categoryDto;
    }

    public static List<CategoryDto> toCategoryDtoList(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();
        for (Category category: categories) {
            result.add(toCategoryDto(category));
        }
        return result;
    }
}
