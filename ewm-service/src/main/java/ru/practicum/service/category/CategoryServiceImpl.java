package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.CategoryValidationException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(Category category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new CategoryValidationException("Отсутствует название категории");
        }
        category = categoryRepository.save(category);
        return category;
    }

    @Override
    public Category update(Category category) {
        categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        if (category.getName().isEmpty()) {
            throw new CategoryValidationException("Отсутствует название категории");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return categoryRepository.findCategories(getPageableAsc(from, size)).getContent();
    }

    public static Pageable getPageableDesc(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }

    public static Pageable getPageableAsc(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "mame");
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }
}
