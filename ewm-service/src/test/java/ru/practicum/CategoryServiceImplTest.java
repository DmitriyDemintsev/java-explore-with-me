package ru.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.CategoryValidationException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.category.CategoryServiceImpl;

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
        Category saveCategory = new Category(0L, "Концерты");
        when(categoryRepository.save(saveCategory)).thenReturn(saveCategory);

        Category actualCategory = categoryService.create(saveCategory);

        assertEquals(saveCategory, actualCategory);
        verify(categoryRepository).save(saveCategory);
    }

    @Test
    void create_whenCategoryNameNotValid_thenCategoryValidationException() {
        Category saveCategory = new Category(0L, "");

        assertThrows(CategoryValidationException.class, () -> categoryService.create(saveCategory));
        verify(categoryRepository, never()).save(saveCategory);
    }

    @Test
    void update_whenCategoryFound_thenUpdatedOnlyAvailableFields() {
        Long categoryId = 0L;
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
        Long categoryId = 0L;
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
        Category category = new Category(0L, "Концерты");

        categoryRepository.deleteById(category.getId());
        assertNull(categoryRepository.getById(category.getId()));
    }

    @Test
    void getCategoryById_whenCategoryFound_thenReturnedCategory() {
        Long categoryId = 0L;
        Category expectedCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        Category actualCategory = categoryService.getCategoryById(categoryId);

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getCategoryById_whenCategoryNotFound_thenCategoryNotFoundException() {
        Long categoryId = 0L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void getCategories() {
        Category fistCategory = new Category(0L, "Концерты");
        Category secondCategory = new Category(1L, "Выставки");
        Category thirdCategory = new Category(2L, "Кино");

        List<Category> expectedCategories = List.of(fistCategory, secondCategory, thirdCategory);
        when(categoryRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(expectedCategories));

        List<Category> actualСategories = categoryService.getCategories(0, 10);

        assertEquals(expectedCategories, actualСategories);
    }
}
