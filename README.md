# SERVER SENT EVENT WITH SPRING BOOT AND ANGULAR
Send notifications to user(s) by subscribing to Server-Sent Events (SSE) using Spring Boot and Angular. This setup enables real-time communication between the server and the client, allowing notifications to be pushed instantly to users.

## OVERVIEW
- Spring Boot: Manages the backend, including handling SSE subscriptions and sending notifications.
- Angular: Manages the frontend, including subscribing to SSE and displaying notifications.

## HOW TO RUN THE APPLICATION
### SPRING BOOT
- Run 
```
mvn spring-boot:run
```  
or 
```
.\mvnw spring-boot:run
```
- Register or Login user to generate token
```
http://localhost:8080/api/v1/auth/register

http://localhost:8080/api/v1/auth/authenticate
```
- Subscribe to notification with the bearer token generated above
```
http://localhost:8080/api/v1/notification/subscribe
```
- Send notification to a user
```
http://localhost:8080/api/v1/notification/notify/:userId
```

### ANGULAR
- Navigate to the frontend directory
```
cd frontend
```
- Install dependencies 
```
npm install
```
- Run 
```
ng serve
```