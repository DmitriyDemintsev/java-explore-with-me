package ru.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.CategoryValidationException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.category.CategoryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Test
    void create_whenCategoryNameValid_thenSavedCategory() {
        Category saveCategory = new Category(0, "Концерты");
        when(categoryRepository.save(saveCategory)).thenReturn(saveCategory);

        Category actualCategory = categoryService.create(saveCategory);

        assertEquals(saveCategory, actualCategory);
        verify(categoryRepository).save(saveCategory);
    }

    @Test
    void create_whenCategoryNameNotValid_thenCategoryValidationException() {
        Category saveCategory = new Category(0, "");

        assertThrows(CategoryValidationException.class, () -> categoryService.create(saveCategory));
        verify(categoryRepository, never()).save(saveCategory);
    }

    @Test
    void update_whenCategoryFound_thenUpdatedOnlyAvailableFields() {
        int categoryId = 0;
        Category oldCategory = new Category();
        oldCategory.setId(categoryId);
        oldCategory.setName("Концерты");

        Category newCategory = new Category();
        newCategory.setId(oldCategory.getId());
        newCategory.setName("Open air");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(oldCategory));

        Category actualCategory = categoryService.update(newCategory);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category savedCategory = categoryArgumentCaptor.getValue();

        assertEquals("Open air", savedCategory.getName());
    }

    @Test
    void update_whenCategoryNameNotValid_thenCategoryValidationException() {
        int categoryId = 0;
        Category oldCategory = new Category();
        oldCategory.setId(categoryId);
        oldCategory.setName("Концерты");

        Category newCategory = new Category();
        newCategory.setId(oldCategory.getId());
        newCategory.setName("");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(oldCategory));

        assertThrows(CategoryValidationException.class, () -> categoryService.update(newCategory));
        verify(categoryRepository, never()).save(newCategory);
    }

    @Test
    void deleteUser() {
        Category category = new Category(0, "Концерты");

        categoryRepository.deleteById(category.getId());
        assertNull(categoryRepository.getById(category.getId()));
    }

    @Test
    void getCategoryById_whenCategoryFound_thenReturnedCategory() {
        int categoryId = 0;
        Category expectedCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        Category actualCategory = categoryService.getCategoryById(categoryId);

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getCategoryById_whenCategoryNotFound_thenCategoryNotFoundException() {
        int categoryId = 0;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void getCategories() {
        Category fistCategory = new Category(0, "Концерты");
        Category secondCategory = new Category(1, "Выставки");
        Category thirdCategory = new Category(2, "Кино");

        List<Category> expectedCategories = List.of(fistCategory, secondCategory, thirdCategory);
        when(categoryRepository.findCategories(any())).thenReturn(new PageImpl<>(expectedCategories));

        List<Category> actualСategories = categoryService.getCategories(0, 10);

        assertEquals(expectedCategories, actualСategories);
    }
}
