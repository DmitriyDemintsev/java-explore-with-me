package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.CategoryAlreadyExistException;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.CategoryValidationException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new CategoryValidationException("Отсутствует название категории");
        }
        if (category.getName().length() > 50) {
            throw new CategoryValidationException("Название категории более 50 символов");
        }
        List<Category> categories = categoryRepository.findAll();
        for (Category category1 : categories) {
            if (Objects.equals(category1.getName(), category.getName())) {
                throw new CategoryAlreadyExistException("Категория с таким названием уже существует");
            }
        }
        category = categoryRepository.save(category);
        return category;
    }

    @Override
    public Category update(Category category) {
        categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        if (category.getName().isBlank()) {
            throw new CategoryValidationException("Отсутствует название категории");
        }
        if (category.getName().length() > 50) {
            throw new CategoryValidationException("Название категории более 50 символов");
        }
        List<Category> categories = categoryRepository.findAll();
        for (Category category1 : categories) {
            if (category1.getName().equals(category.getName()) && !category1.getId().equals(category.getId())) {
                throw new CategoryAlreadyExistException("Категория с таким названием уже существует");
            }
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(long id) {
        List<Category> categories = categoryRepository.findCategoryRelatedToEvents(categoryRepository.findById(id));
        if (categories.size() > 0) {
            throw new CategoryAlreadyExistException("Категория используется");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return categoryRepository.findAll(getPageableAsc(from, size)).getContent();
    }

    public static Pageable getPageableDesc(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }

    public static Pageable getPageableAsc(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }
}
