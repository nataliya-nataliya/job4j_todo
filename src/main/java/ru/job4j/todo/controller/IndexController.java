package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.service.UserService;

@AllArgsConstructor
@Controller
public class IndexController {

    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model) {
        var allTasks = taskService.findAllOrderById();
        model.addAttribute("tasks", allTasks);
        if (allTasks.isEmpty()) {
            model.addAttribute("error", "To Do Task list is empty. Add new Task");
            return "tasks/all";
        }
        return "index";
    }
}
