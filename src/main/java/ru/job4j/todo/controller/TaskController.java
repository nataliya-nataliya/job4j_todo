package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        model.addAttribute("tasks", taskService.findAllOrderById(userCurrentTimeZone));
        if (taskService.findAllOrderById(userCurrentTimeZone).isEmpty()) {
            model.addAttribute("error", "To Do Task list is empty. Add new Task");
            return "tasks/all";
        }
        model.addAttribute("tasks",
                new ArrayList<>(taskService.findAllOrderById(userCurrentTimeZone)));
        return "tasks/all";
    }

    @GetMapping("/done")
    public String getAllWhereDoneIsTrue(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        model.addAttribute("tasks",
                new ArrayList<>(taskService.findByDoneOrderById(true, userCurrentTimeZone)));
        return "tasks/done";
    }

    @GetMapping("/undone")
    public String getAllWhereDoneIsFalse(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        model.addAttribute("tasks",
                new ArrayList<>(taskService.findByDoneOrderById(false, userCurrentTimeZone)));
        return "tasks/undone";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        var taskOptional = taskService.findById(id, userCurrentTimeZone);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task with the specified ID was not found");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/{id}")
    public String updateDone(@ModelAttribute Task task, Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        task.setUser(user);
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
        model.addAttribute("categories", categoryService.findAllOrderById());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model, HttpSession session,
                         @RequestParam(value = "categories", required = false)
                         List<Integer> selectedIdCategories) {
        if (selectedIdCategories == null) {
            model.addAttribute("priorities", priorityService.findAllOrderByPosition());
            model.addAttribute("categories", categoryService.findAllOrderById());
            model.addAttribute("error", "You must select at least 1 category");
            return "tasks/create";
        }
        task.setTaskCategoryList(new HashSet<>(categoryService
                .findByMultipleIdsOrderById(selectedIdCategories)));
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
    public String editById(Model model, @PathVariable int id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var userCurrentTimeZone = user.getTimezone();
        model.addAttribute("priorities", priorityService.findAllOrderByPosition());
        var taskOptional = taskService.findById(id, userCurrentTimeZone);
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
