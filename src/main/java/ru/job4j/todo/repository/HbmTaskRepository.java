package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public Optional<Task> save(Task task) {
        Optional<Task> optionalTask = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            optionalTask = Optional.of(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return optionalTask;
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = 0;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            affectedRows = session.createQuery(
                            "delete Task where id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return affectedRows > 0;
    }

    public boolean update(Task task) {
        boolean isCompletedTransaction = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "update Task set name = :fName, description = :fDescription,"
                                    + "created = :fCreated, done = :fDone where id = :fId")
                    .setParameter("fName", task.getName())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId());
            session.update(task);
            session.getTransaction().commit();
            isCompletedTransaction = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return isCompletedTransaction;
    }

    @Override
    public boolean updateDone(Task task) {
        boolean isCompletedTransaction = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "update Task set done = :fDone where id = :fId")
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId());
            session.update(task);
            session.getTransaction().commit();
            isCompletedTransaction = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return isCompletedTransaction;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> optionalTask = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            optionalTask = session.createQuery(
                            "from Task where id = : fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return optionalTask;
    }

    @Override
    public Collection<Task> findAllOrderById() {
        Session session = sf.openSession();
        List<Task> taskList = new ArrayList<>();
        try {
            session.beginTransaction();
            taskList = session.createQuery(
                    "from Task order by id", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return taskList;
    }

    @Override
    public Collection<Task> findByDoneOrderById(boolean done) {
        Session session = sf.openSession();
        List<Task> taskList = new ArrayList<>();
        try {
            session.beginTransaction();
            taskList = session.createQuery(
                            "from Task where done = : fDone order by id", Task.class)
                    .setParameter("fDone", done)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return taskList;
    }
}
