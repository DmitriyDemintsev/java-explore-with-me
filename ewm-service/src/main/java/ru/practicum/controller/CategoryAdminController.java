package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createUser(@Valid @RequestBody CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryService.create(CategoryMapper.toCategory(categoryDto, null)));
    }

    @PatchMapping("/{id}")
    public CategoryDto updateUser(@Valid @RequestBody CategoryDto categoryDto,
                              @PathVariable("id") Long id) {
        return CategoryMapper.toCategoryDto(categoryService.update(CategoryMapper.toCategory(categoryDto, id)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        categoryService.delete(id);
    }
}
