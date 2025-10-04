package com.example.task_service.service;

import com.example.task_service.model.Task;
import com.example.task_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // 1. Add task
    public Task addTask(Task task){
        return taskRepository.save(task);
    }

    // 2 & 3. Get all tasks for a user
    public List<Task> getUserTasks(String username){
        return taskRepository.findByUsername(username);
    }

    // 4. Mark task completed
    public Task markTaskCompleted(Long taskId, String username){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if(!task.getUsername().equals(username)) throw new RuntimeException("Unauthorized");
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    // 5. Edit task
    public Task updateTask(Task task){
        return taskRepository.save(task);
    }

    // 6. Search tasks by title
    public List<Task> searchTasks(String username, String keyword){
        return taskRepository.findByUsername(username)
                .stream()
                .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // 7. List pending tasks
    public List<Task> getPendingTasks(String username){
        return taskRepository.findByUsernameAndCompleted(username, false);
    }
 @GetMapping("/tasks/hello")
    public String hello() {
        return " Token verified! You can access tasks now.";
    }
    // 8. List completed tasks
    public List<Task> getCompletedTasks(String username){
        return taskRepository.findByUsernameAndCompleted(username, true);
    }

    // 9. Delete all tasks
    public void deleteAllTasks(String username){
        List<Task> tasks = taskRepository.findByUsername(username);
        tasks.forEach(t -> taskRepository.deleteById(t.getId()));
    }

    // 2. Delete single task
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
