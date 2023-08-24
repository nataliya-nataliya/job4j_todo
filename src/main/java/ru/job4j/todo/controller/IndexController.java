package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
public class IndexController {

    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model, HttpSession session) {
        model.addAttribute("tasks", taskService.findAllOrderById());
        if (taskService.findAllOrderById().isEmpty()) {
            model.addAttribute("error", "To Do Task list is empty. Add new Task");
            return "tasks/all";
        }
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        List<Task> taskList = new ArrayList<>(taskService.findAllOrderById());
        for (Task task : taskList) {
            var userCreatedTaskTimeZone = task.getUser().getTimezone();
            LocalDateTime created = task.getCreated();
            if (!userCurrentTimeZone.equals(userCreatedTaskTimeZone)) {
                ZonedDateTime zonedCreated = created.atZone(ZoneId.of(userCreatedTaskTimeZone));
                ZonedDateTime convertedTime = zonedCreated
                        .withZoneSameInstant(ZoneId.of(userCurrentTimeZone));
                task.setCreated(convertedTime.toLocalDateTime());
            }
        }
        model.addAttribute("tasks", taskList);
        return "index";
    }
}
