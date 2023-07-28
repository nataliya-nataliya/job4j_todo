package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.service.TaskService;

@AllArgsConstructor
@Controller
public class IndexController {

    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderById());
        if (taskService.findAllOrderById().isEmpty()) {
            model.addAttribute("error", "To Do Task list is empty. Add new Task");
            return "tasks/all";
        }
        return "index";
    }
}
