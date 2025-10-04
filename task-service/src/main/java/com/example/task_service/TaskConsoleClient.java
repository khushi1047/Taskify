package com.example.task_service;

import com.example.task_service.model.Task;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TaskConsoleClient {

private static final String USER_SERVICE_URL = "http://localhost:8080/users/login";
private static final String TASK_SERVICE_URL = "http://localhost:8080/tasks";


    private static String jwtToken = null;
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Login
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            jwtToken = login(username, password);
            System.out.println("Login successful! JWT Token obtained.\n");
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\n========== TASK MANAGER ==========");
            System.out.println("1. Add a task");
            System.out.println("2. Remove a task");
            System.out.println("3. List all tasks");
            System.out.println("4. Mark task completed");
            System.out.println("5. Edit task");
            System.out.println("6. Search tasks by title");
            System.out.println("7. List pending tasks");
            System.out.println("8. List completed tasks");
            System.out.println("9. Delete all tasks");
            System.out.println("10. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addTask(scanner);
                case 2 -> removeTask(scanner);
                case 3 -> listAllTasks();
                case 4 -> markTaskCompleted(scanner);
                case 5 -> editTask(scanner);
                case 6 -> searchTasks(scanner);
                case 7 -> listPendingTasks();
                case 8 -> listCompletedTasks();
                case 9 -> deleteAllTasks();
                case 10 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

   private static String login(String username, String password) {

    Map<String, String> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(USER_SERVICE_URL, request, Map.class);

    if (response.getStatusCode() == HttpStatus.OK) {
        // Assuming your user-service returns {"token":"..."}
        return (String) response.getBody().get("token");
    } else {
        throw new RuntimeException("Login failed: " + response.getStatusCode());
    }
}


    private static HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String desc = scanner.nextLine();

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);

        HttpEntity<Task> entity = new HttpEntity<>(task, getAuthHeaders());
        ResponseEntity<Task> response = restTemplate.postForEntity(TASK_SERVICE_URL + "/add", entity, Task.class);

        System.out.println("Added task: " + response.getBody().getTitle());
    }

    private static void removeTask(Scanner scanner) {
        System.out.print("Enter task ID to delete: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        restTemplate.exchange(TASK_SERVICE_URL + "/delete/" + id, HttpMethod.DELETE, entity, String.class);

        System.out.println("Deleted task ID: " + id);
    }

    private static void listAllTasks() {
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/all",
                HttpMethod.GET, entity, Task[].class);

        List<Task> tasks = Arrays.asList(response.getBody());
        tasks.forEach(t -> System.out.println(t.getId() + ": " + t.getTitle() + " [" + (t.isCompleted() ? "Completed" : "Pending") + "]"));
    }

    private static void markTaskCompleted(Scanner scanner) {
        System.out.print("Enter task ID to mark completed: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        // Fetch task
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/all",
                HttpMethod.GET, entity, Task[].class);

        Task task = Arrays.stream(response.getBody())
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(task != null){
            task.setCompleted(true);
            HttpEntity<Task> updateEntity = new HttpEntity<>(task, getAuthHeaders());
            restTemplate.exchange(TASK_SERVICE_URL + "/update", HttpMethod.PUT, updateEntity, Task.class);
            System.out.println("Marked task completed: " + task.getTitle());
        } else {
            System.out.println("Task not found!");
        }
    }

    private static void editTask(Scanner scanner) {
        System.out.print("Enter task ID to edit: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/all",
                HttpMethod.GET, entity, Task[].class);

        Task task = Arrays.stream(response.getBody())
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(task != null){
            System.out.print("Enter new title: ");
            task.setTitle(scanner.nextLine());
            System.out.print("Enter new description: ");
            task.setDescription(scanner.nextLine());

            HttpEntity<Task> updateEntity = new HttpEntity<>(task, getAuthHeaders());
            restTemplate.exchange(TASK_SERVICE_URL + "/update", HttpMethod.PUT, updateEntity, Task.class);

            System.out.println("Task updated successfully!");
        } else {
            System.out.println("Task not found!");
        }
    }

    private static void searchTasks(Scanner scanner) {
        System.out.print("Enter keyword to search in title: ");
        String keyword = scanner.nextLine();

        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/all",
                HttpMethod.GET, entity, Task[].class);

        Arrays.stream(response.getBody())
                .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .forEach(t -> System.out.println(t.getId() + ": " + t.getTitle() + " [" + (t.isCompleted() ? "Completed" : "Pending") + "]"));
    }

    private static void listPendingTasks() {
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/pending",
                HttpMethod.GET, entity, Task[].class);

        Arrays.stream(response.getBody())
                .forEach(t -> System.out.println(t.getId() + ": " + t.getTitle()));
    }

    private static void listCompletedTasks() {
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/completed",
                HttpMethod.GET, entity, Task[].class);

        Arrays.stream(response.getBody())
                .forEach(t -> System.out.println(t.getId() + ": " + t.getTitle()));
    }

    private static void deleteAllTasks() {
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Task[]> response = restTemplate.exchange(TASK_SERVICE_URL + "/all",
                HttpMethod.GET, entity, Task[].class);

        for(Task t : response.getBody()){
            restTemplate.exchange(TASK_SERVICE_URL + "/delete/" + t.getId(), HttpMethod.DELETE, entity, String.class);
        }

        System.out.println("All tasks deleted!");
    }
}
