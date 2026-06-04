# Ithra — Articles Platform Backend

Spring Boot 4.1.0 / Java 25 REST API for the Ithra articles platform.

---

## Prerequisites

- Java 25
- Maven 3.9+
- MySQL 8+

---

## Database Setup

```sql
CREATE DATABASE articles_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'articles_db'@'localhost' IDENTIFIED BY 'rIfEGFL5$';
GRANT ALL PRIVILEGES ON articles_db.* TO 'articles_db'@'localhost';
FLUSH PRIVILEGES;
```

Tables are auto-created by Hibernate on first run (`ddl-auto=update`).

---

## Mail Configuration (Gmail)

1. Enable 2-Step Verification on your Google account.
2. Go to **Google Account → Security → App passwords** and generate a password.
3. Update `application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-16-char-app-password
```

---

## JWT Secret

Replace the placeholder in `application.properties` with a secure random string (minimum 32 characters):

```properties
jwt.secret=change-this-to-a-very-long-random-secret-key-at-least-32-chars
```

---

## Running the Application

```bash
./mvnw spring-boot:run
```

The server starts at **http://localhost:8080**.

---

## Swagger UI

Open **http://localhost:8080/swagger-ui.html** in your browser.

Click **Authorize** and enter your JWT token (`Bearer <token>`) to test protected endpoints.

---

## Uploads

Uploaded images are stored in the `uploads/` folder (relative to the working directory) and served at:

```
http://localhost:8080/uploads/<filename>
```

---

## Key Endpoints

| Group           | Base Path                         |
|-----------------|-----------------------------------|
| Auth            | `POST /api/auth/register`         |
| Articles        | `GET /api/articles`               |
| Categories      | `GET /api/categories`             |
| Tags            | `GET /api/tags`                   |
| Comments        | `GET /api/articles/{id}/comments` |
| Upload          | `POST /api/upload/image`          |
| Admin — Users   | `GET /api/admin/users`            |
| Admin — Stats   | `GET /api/admin/stats/overview`   |

See Swagger UI for full documentation.

---

## Project Structure

```
src/main/java/com/omarrdev/ithra/
├── config/          SecurityConfig, WebMvcConfig, OpenApiConfig
├── controller/      REST controllers
├── dto/             request/ and response/ records
├── entity/          JPA entities
├── enums/           Role, ArticleStatus
├── exception/       GlobalExceptionHandler, custom exceptions
├── mapper/          MapStruct interfaces
├── repository/      Spring Data JPA repositories
├── security/        JwtService, JwtAuthFilter, UserDetailsService
├── service/         Business logic
└── util/            SlugUtil
```

---

## Notes

- Spring Boot 4.1.0-RC1 requires the Spring Milestones repository (already configured in `pom.xml`).
- Springdoc version `2.8.9` is included; if a compatibility issue arises with Spring Boot 4.x, update to the latest `3.x` release.
- All deletes are soft deletes — records are never physically removed from the database.
