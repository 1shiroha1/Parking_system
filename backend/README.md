# Smart Parking System (Spring Boot)

## Requirements
- Java 8
- Maven
- MySQL 8.0.19

## Database
1. Create database: `parking_system`
   ```sql
   CREATE DATABASE IF NOT EXISTS parking_system
     CHARACTER SET utf8mb4
     COLLATE utf8mb4_unicode_ci;
   ```
2. Update `spring.datasource` in `src/main/resources/application.yml` if your MySQL credentials differ.

Hibernate will auto-create/update tables (`spring.jpa.hibernate.ddl-auto=update`).

## Run
From `backend/`:
```bash
mvn spring-boot:run
```

## Default accounts
- Admin: `admin / admin123`
- Repair worker: `repair / repair123`

