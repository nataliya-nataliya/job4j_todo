package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;

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
        model.addAttribute("tasks", taskService.findByDoneOrderById(true));
        return "tasks/done";
    }

    @GetMapping("/undone")
    public String getAllWhereDoneIsFalse(Model model) {
        model.addAttribute("tasks", taskService.findByDoneOrderById(false));
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
    public String updateDone(@ModelAttribute Task task, Model model) {
        task.setDone(!task.isDone());
        if (!taskService.updateDone(task)) {
            model.addAttribute("message", "Task status not updated");
            return "errors/404";
        }
        return String.format("redirect:/tasks/%d", task.getId());
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAllOrderByPosition());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model, HttpSession session) {
        task.setUser((User) session.getAttribute("user"));
        var taskOptional = taskService.save(task);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task not created");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Task with the specified ID was not found");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editById(Model model, @PathVariable int id) {
        model.addAttribute("priorities", priorityService.findAllOrderByPosition());
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task with the specified ID was not found");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, HttpSession session) {
        task.setUser((User) session.getAttribute("user"));
        if (!taskService.update(task)) {
            model.addAttribute("message", "Task not updated");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
