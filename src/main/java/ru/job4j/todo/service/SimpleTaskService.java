package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;

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
    public Optional<TaskDto> findById(int id, String userCurrentTimeZone) {
        return convertCreatedOfTask(taskRepository.findById(id), userCurrentTimeZone);
    }

    @Override
    public Collection<TaskDto> findAllOrderById(String userCurrentTimeZone) {
        return convertCreatedOfTasksList(taskRepository.findAllOrderById(), userCurrentTimeZone);
    }

    @Override
    public Collection<TaskDto> findByDoneOrderById(boolean done, String userCurrentTimeZone) {
        return convertCreatedOfTasksList(taskRepository
                .findByDoneOrderById(done), userCurrentTimeZone);
    }

    private Collection<TaskDto> convertCreatedOfTasksList(Collection<Task> taskList,
                                                          String userCurrentTimeZone) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : taskList) {
            var userCreatedTaskTimeZone = task.getUser().getTimezone();
            LocalDateTime created = task.getCreated();
            if (!userCurrentTimeZone.equals(userCreatedTaskTimeZone)) {
                ZonedDateTime zonedCreated = created.atZone(ZoneId.of(userCreatedTaskTimeZone));
                ZonedDateTime convertedTime = zonedCreated
                        .withZoneSameInstant(ZoneId.of(userCurrentTimeZone));
                task.setCreated(convertedTime.toLocalDateTime());
            }
            taskDtoList.add(new TaskDto(task.getId(), task.getName(), task.getDescription(),
                    created, task.getCreated(), task.isDone(), task.getUser(), task.getPriority()));
        }
        return taskDtoList;
    }

    private Optional<TaskDto> convertCreatedOfTask(Optional<Task> optionalTask,
                                                   String userCurrentTimeZone) {
            var task = optionalTask.get();
            var userCreatedTaskTimeZone = task.getUser().getTimezone();
            LocalDateTime created = task.getCreated();
            if (!userCurrentTimeZone.equals(userCreatedTaskTimeZone)) {
                ZonedDateTime zonedCreated = created.atZone(ZoneId.of(userCreatedTaskTimeZone));
                ZonedDateTime convertedTime = zonedCreated
                        .withZoneSameInstant(ZoneId.of(userCurrentTimeZone));
                task.setCreated(convertedTime.toLocalDateTime());
            }
            return Optional.of(new TaskDto(task.getId(), task.getName(), task.getDescription(),
                    created, task.getCreated(), task.isDone(), task.getUser(), task.getPriority()));
    }
}
