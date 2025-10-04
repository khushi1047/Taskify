package com.example.task_service.controller;

import com.example.task_service.model.Task;
import com.example.task_service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public Task addTask(@RequestBody Task task, Authentication auth){
        task.setUsername(auth.getName());
        return taskService.addTask(task);
    }

    @GetMapping("/all")
    public List<Task> getAllTasks(Authentication auth){
        return taskService.getUserTasks(auth.getName());
    }

    @GetMapping("/pending")
    public List<Task> getPendingTasks(Authentication auth){
        return taskService.getPendingTasks(auth.getName());
    }

    @GetMapping("/completed")
    public List<Task> getCompletedTasks(Authentication auth){
        return taskService.getCompletedTasks(auth.getName());
    }

    @PutMapping("/update")
    public Task updateTask(@RequestBody Task task, Authentication auth){
        task.setUsername(auth.getName());
        return taskService.updateTask(task);
    }

    @PutMapping("/complete/{id}")
    public Task markTaskCompleted(@PathVariable Long id, Authentication auth){
        return taskService.markTaskCompleted(id, auth.getName());
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return "Deleted task: " + id;
    }

    @DeleteMapping("/deleteAll")
    public String deleteAllTasks(Authentication auth){
        taskService.deleteAllTasks(auth.getName());
        return "All tasks deleted!";
    }

    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String keyword, Authentication auth){
        return taskService.searchTasks(auth.getName(), keyword);
    }
}
