package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> findAllOrderById() {
        return crudRepository.query("from Category order by id", Category.class);
    }

    @Override
    public Collection<Category> findByMultipleIdsOrderById(List<Integer> categoriesIdList) {
        return crudRepository.query(
                "from Category f where f.id in :fIdList", Category.class,
                Map.of("fIdList", categoriesIdList)
        );
    }
}
