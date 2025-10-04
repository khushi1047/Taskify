## Overview
A **Task Manager Application** with **User Authentication** and **JWT-based Security**.  
Users can register, login, and manage tasks (add, edit, delete, mark completed/pending, search) via **console** or **REST APIs**.  

---

## Features
- ✅ User Registration and Login with JWT  
- ✅ Add, Edit, Delete, List Tasks  
- ✅ Mark tasks as completed or pending  
- ✅ Search tasks by title  
- ✅ JWT Authentication for secure access  
- ✅ Console-based task management  

---

## Technologies Used
- **Java 22**  
- **Spring Boot 3.5.6** (Spring Security, Spring Data JPA, Spring MVC)  
- **MySQL 8.0**  
- **Maven 3.9.0**  
- **JWT (JSON Web Tokens)**  
- **RestTemplate** for API calls  
- **Postman** (for API testing)  

---

## Installation
1. Clone the repository:  
   ```bash
   git clone https://github.com/your-username/task-manager.git

2. Navigate to the project directory:
   cd task-manager

3. Configure MySQL databases in application.properties for user-service and task-service.

4. Build the project with Maven:
   mvn clean install

5. Run the services:
   # Run user-service
cd user-service
mvn spring-boot:run

# Run task-service
cd task-service
mvn spring-boot:run

# Run API Gateway
cd api-gateway
mvn spring-boot:run


API-based

Base URLs:

User Service: http://localhost:8081/users

Task Service: http://localhost:8082/tasks

API Gateway: http://localhost:8080

Endpoints:

Method	URL	Description	Auth Required
POST	/users/register	Register a new user	No
POST	/users/login	Login & get JWT	No
POST	/tasks/add	Add a new task	Yes
GET	/tasks/all	List all tasks	Yes
PUT	/tasks/update	Update a task	Yes
DELETE	/tasks/delete/{id}	Delete a task	Yes
GET	/tasks/pending	List pending tasks	Yes
GET	/tasks/completed	List completed tasks	Yes
