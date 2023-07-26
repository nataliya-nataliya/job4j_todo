package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.service.TaskService;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderById());
        return "tasks/all";
    }

    @GetMapping("/done")
    public String getAllWhereDoneIsTrue(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderByIdWhereDoneIsTrue());
        return "tasks/done";
    }

    @GetMapping("/undone")
    public String getAllWhereDoneIsFalse(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderByIdWhereDoneIsFalse());
        return "tasks/undone";
    }
}
