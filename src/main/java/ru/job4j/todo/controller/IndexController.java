package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@AllArgsConstructor
@Controller
public class IndexController {

    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        if (taskService.findAllOrderById(userCurrentTimeZone).isEmpty()) {
            model.addAttribute("error", "To Do Task list is empty. Add new Task");
            return "tasks/all";
        }
        model.addAttribute("tasks",
                new ArrayList<>(taskService.findAllOrderById(userCurrentTimeZone)));
        return "index";
    }
}
