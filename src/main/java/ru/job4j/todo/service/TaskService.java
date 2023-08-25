package ru.job4j.todo.service;

import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Optional<Task> save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    boolean updateDone(Task task);

    Optional<TaskDto> findById(int id, String userCurrentTimeZone);

    Collection<TaskDto> findAllOrderById(String userCurrentTimeZone);

    Collection<TaskDto> findByDoneOrderById(boolean done, String userCurrentTimeZone);

}
