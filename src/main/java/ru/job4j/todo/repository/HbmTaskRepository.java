package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository{
    private final SessionFactory sf;

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    public void update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "update Task set name = :fName, description = :fDescription,"
                                    + "created = :fCreated, done = :fDone where id = :fid")
                    .setParameter("fName", task.getName())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Task> findAllOrderById() {
        return null;
    }
}
