package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Collection<Category> findAllOrderById() {
        return categoryRepository.findAllOrderById();
    }

    @Override
    public Collection<Category> findByMultipleIdsOrderById(List<Integer> categoriesIdList) {
        return categoryRepository.findByMultipleIdsOrderById(categoriesIdList);
    }
}
