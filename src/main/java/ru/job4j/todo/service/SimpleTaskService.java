package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;

    public SimpleTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean updateDone(Task task) {
        return taskRepository.updateDone(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Collection<Task> findAllOrderById() {
        return taskRepository.findAllOrderById();
    }

    @Override
    public Collection<Task> findByDoneOrderById(boolean done) {
        return taskRepository.findByDoneOrderById(done);
    }
}
