package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;
import ru.job4j.todo.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public SimpleTaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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
    public Collection<TaskDto> findAllOrderById() {
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : taskRepository.findAllOrderById()) {
            taskDtoList.add(new TaskDto(
                    task.getId(),
                    task.getName(),
                    formatLocalDateTime(task.getCreated()),
                    task.isDone(),
                    userRepository.findById(task.getUser().getId()).get().getName()));
        }
        return taskDtoList;
    }

    @Override
    public Collection<TaskDto> findByDoneOrderById(boolean done) {

        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : taskRepository.findByDoneOrderById(done)) {
            taskDtoList.add(new TaskDto(
                    task.getId(),
                    task.getName(),
                    formatLocalDateTime(task.getCreated()),
                    task.isDone(),
                    userRepository.findById(task.getUser().getId()).get().getName()));
        }
        return taskDtoList;
    }

    public String formatLocalDateTime(LocalDateTime startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return startTime.format(formatter);
    }
}
