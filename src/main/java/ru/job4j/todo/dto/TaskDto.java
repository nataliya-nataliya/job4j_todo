package ru.job4j.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class TaskDto {
    private int id;
    private String name;
    private String description;
    private LocalDateTime created;
    private LocalDateTime convertedCreated;
    private boolean done;
    private User user;
    private Priority priority;
}
