# Backwoods Labs CRM API

## Overview
This is the Backwoods Labs CRM API, built using **Spring Boot** and **MySQL**. It includes features like JWT-based authentication, Swagger documentation, and email sending via **JavaMailSender**.

## Prerequisites
- **Java 23 or higher**
- **Spring Boot 3.x**
- **MySQL Server** (version 8.x recommended)
- **Google account** with **App Password** for email functionality

## Configuration

### MySQL Setup
- The database is configured to connect to **MySQL**. The default URL is `jdbc:mysql://localhost:3306/backwoods_crm_db?createDatabaseIfNotExist=true`.
- Set up the **username** and **password** in `application-example.yml`. When running the application, create a new `application.yml` file based on this example and provide your actual credentials.

### Email Configuration
- The email service uses **Gmail SMTP**. You need to generate an **App Password** in your Google account for this service.
- Update the following fields in `application-example.yml`:
  - `username`: Your Gmail account (e.g., backwoodslabs39@gmail.com)
  - `password`: Your generated **App Password**

### JWT Configuration
- A **secret key** is used for signing JWT tokens.
- Token expiration time is set to 1 hour.

## How to Run

1. **Clone the repository** and navigate to the project folder.
2. **Configure the database and email settings** in `application.yml` (use `application-example.yml` as a template).
3. **Build and run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access **Swagger UI** at `http://localhost:8080/swagger-ui.html` for API documentation.

## Key Configuration Points
- **Port**: 8080
- **Swagger UI** enabled at `/swagger-ui.html`
- **Flyway** for database migrations
- **JavaMailSender** for email sending via Google SMTP

---

Feel free to reach out for support or more detailed documentation.
