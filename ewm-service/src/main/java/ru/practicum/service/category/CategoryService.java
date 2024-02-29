package ru.practicum.service.category;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Category;

import java.util.List;

@Transactional(readOnly = true)
public interface CategoryService {

    @Transactional
    Category create(Category category);

    @Transactional
    Category update(Category category);

    @Transactional
    void delete(long id);

    Category getCategoryById(long id);

    List<Category> getCategories(int from, int size);
}
