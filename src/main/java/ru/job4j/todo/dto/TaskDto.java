package ru.job4j.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TaskDto {
    private int id;
    private String name;
    private String created;
    private boolean done;
    private String userName;
}
