package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> save(Task task) {
        Optional<Task> optionalUser;
        try {
            crudRepository.run(session -> session.persist(task));
            optionalUser = Optional.of(task);
        } catch (PersistenceException e) {
            optionalUser = Optional.empty();
        }
        return optionalUser;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isCompletedTransaction;
        try {
            crudRepository.run(
                    "delete Task where id = :fId",
                    Map.of("fId", id)
            );
            isCompletedTransaction = true;
        } catch (PersistenceException e) {
            isCompletedTransaction = false;
        }
        return isCompletedTransaction;
    }

    public boolean update(Task task) {
        boolean isCompletedTransaction;
        try {
            crudRepository.run(session -> session.merge(task));
            isCompletedTransaction = true;
        } catch (PersistenceException e) {
            isCompletedTransaction = false;
        }
        return isCompletedTransaction;
    }

    @Override
    public boolean updateDone(Task task) {
        boolean isCompletedTransaction;
        try {
            crudRepository.run(
                    "update Task set done = :fDone where id = :fId",
                    Map.of("fDone", task.isDone(),
                            "fId", task.getId())
            );
            isCompletedTransaction = true;
        } catch (PersistenceException e) {
            isCompletedTransaction = false;
        }
        return isCompletedTransaction;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task where id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public Collection<Task> findAllOrderById() {
        return crudRepository.query("from Task order by id", Task.class);
    }

    @Override
    public Collection<Task> findByDoneOrderById(boolean done) {
        return crudRepository.query(
                "from Task where done = : fDone order by id", Task.class,
                Map.of("fDone", done)
        );
    }
}
