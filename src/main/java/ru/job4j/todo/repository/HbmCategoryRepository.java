package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> findAllOrderById() {
        return crudRepository.query("from Category order by id", Category.class);
    }

    @Override
    public Optional<Category> findById(int id) {
        return crudRepository.optional(
                "from Category f where f.id = :fId", Category.class,
                Map.of("fId", id)
        );
    }
}
