# Clothery
### Overview
Clothery is a modern e-commerce web app for fashion lovers. It offers stylish clothing for men and women and provides a seamless shopping experience with intuitive navigation and secure payments.

This is a backend project built with Spring Boot to demonstrate RESTful API design, database interaction, authentication, and authorization.

#

### Main features
- User registration & login (Auth0)
- CRUD operations

#

### Tech Stack
- Backend: Spring Boot 3.4.4, Spring Web, Spring Security, Auth0, JPA (Hibernate)
- Database: PostgreSQL
- Restful API Documentation: Swagger UI
- Language: Java 21
- Cloud: Cloudinary

#

### ERD 
<img width="586" height="350" alt="clothery_erd" src="https://github.com/user-attachments/assets/cb74979a-167d-4c67-b810-f601ef22a403" />

### Getting Started
1. Clone the repository
   
   ```
   git clone https://github.com/Bfilahi/clothery-backend.git
   ```

2. Database Setup

   * Create a PostgreSQL database.
   * Update application.properties:
     
     ```
      spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
      spring.datasource.username=yourusername
      spring.datasource.password=yourpassword
     ```

3. Run the application

   ```
   cd clothery-backend
   mvn spring-boot:run
   ```

#

### Restful API Documentation
#### Swagger
Once the application is running, it will be available at: http://localhost:8080/swagger-ui/index.html

#

### Author & License
&copy; 2026 <strong> Bfilahi </strong>
This project is for interview and technical demonstration purposes only. </br>
Not intended for commercial use.
