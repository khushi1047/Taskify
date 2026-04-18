📋 Task Manager (JWT Secured)
📌 Overview

A Task Manager Application with JWT-based authentication that allows users to register, login, and manage tasks efficiently via console or REST APIs.

🚀 Features
🔐 User Registration & Login (JWT Secured)
📝 Add, Edit, Delete, and List Tasks
✅ Mark tasks as Completed / Pending
🔍 Search tasks by title
🔄 Secure API access using JWT
💻 Console-based task management
🛠️ Technologies Used
Java 22
Spring Boot 3.5.6 (Spring Security, Spring Data JPA, Spring MVC)
MySQL 8.0
Maven 3.9.0
JWT (JSON Web Tokens)
RestTemplate (API communication)
Postman (API testing)
⚙️ Installation & Setup
Clone Repository
git clone https://github.com/your-username/task-manager.git
cd task-manager
Configure Database

Update application.properties in both services:

spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password
Build Project
mvn clean install
▶️ Run Services
# Run User Service
cd user-service
mvn spring-boot:run

# Run Task Service
cd ../task-service
mvn spring-boot:run

# Run API Gateway
cd ../api-gateway
mvn spring-boot:run
🌐 API Base URLs
User Service: http://localhost:8081/users
Task Service: http://localhost:8082/tasks
API Gateway: http://localhost:8080
📡 API Endpoints
User APIs
Method	Endpoint	Description
POST	/users/register	Register new user
POST	/users/login	Login & get JWT
Task APIs
Method	Endpoint	Description
POST	/tasks/add	Add new task
GET	/tasks/all	List all tasks
PUT	/tasks/update	Update task
DELETE	/tasks/delete/{id}	Delete task
GET	/tasks/pending	List pending tasks
GET	/tasks/completed	List completed tasks
🔐 Authentication
Authorization: Bearer <your_token>
⭐ Support

If you like this project, give it a ⭐ on GitHub!
