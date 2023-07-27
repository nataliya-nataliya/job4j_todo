package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderById());
        if (taskService.findAllOrderById().isEmpty()) {
            model.addAttribute("error", "To Do Task list is empty. Add new Task");
            return "tasks/all";
        }
        return "tasks/all";
    }

    @GetMapping("/done")
    public String getAllWhereDoneIsTrue(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderByIdWhereDoneIsTrue());
        if (taskService.findAllOrderByIdWhereDoneIsTrue().isEmpty()) {
            model.addAttribute("error", "Completed tasks list is empty");
            return "tasks/done";
        }
        return "tasks/done";
    }

    @GetMapping("/undone")
    public String getAllWhereDoneIsFalse(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderByIdWhereDoneIsFalse());
        return "tasks/undone";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task with the specified ID was not found");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/{id}")
    public String postById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "There is no such task id");
            return "errors/404";
        }
        Task task = taskOptional.get();
        task.setDone(!task.isDone());
        taskService.update(task);
        return String.format("redirect:/tasks/%d", task.getId());
    }
}
