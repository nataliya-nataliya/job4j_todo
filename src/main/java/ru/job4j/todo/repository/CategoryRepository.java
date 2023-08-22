package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository {
    Collection<Category> findAllOrderById();

    Collection<Category> findByMultipleIdsOrderById(List<Integer> categoriesIdList);

}
