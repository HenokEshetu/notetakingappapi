# NoteTakingApp API

A secure, modern RESTful API for encrypted note-taking, built with Spring Boot, JPA, JWT authentication, and strong cryptography (AES & RSA). Designed for privacy, extensibility, and ease of integration with frontend clients.

---
[![Watch the video](https://img.youtube.com/vi/QkF1usPAFcA/hqdefault.jpg)](https://www.youtube.com/watch?v=QkF1usPAFcA)
---

## 🚀 Features

- **User Registration & Authentication**: Secure JWT-based login and registration.
- **Role-Based Access Control**: Supports `USER`, `MANAGER`, and `ADMIN` roles with fine-grained permissions.
- **Encrypted Notes**: Notes are encrypted per-user using AES symmetric keys, which are themselves protected with RSA and layered encryption.
- **Key Management**: Each user has a unique key pair and symmetric key, securely stored and managed.
- **RESTful Endpoints**: CRUD operations for users and notes, with clear, versioned API paths.
- **CORS Support**: Configured for easy frontend integration (e.g., React, Vue).
- **Extensible Security**: Easily add more roles, permissions, or authentication providers.
- **Docker-Ready**: Simple Dockerfile for containerized deployment.
- **PostgreSQL Integration**: Uses PostgreSQL for persistent storage.

---

## 🏗️ Architecture Overview

- **Spring Boot**: Application framework.
- **Spring Security**: Authentication, authorization, and JWT filter chain.
- **JPA/Hibernate**: ORM for PostgreSQL.
- **BouncyCastle**: Cryptographic provider for RSA/AES.
- **Lombok**: Boilerplate reduction.
- **Layered Structure**: Controllers → Services → Repositories → Entities.

---

## 📦 Project Structure

```
src/
  main/
    java/com/notetakingapp/api/
      auth/         # Authentication & registration logic
      config/       # Security, JWT, and app configuration
      crypto/       # AES & RSA encryption utilities
      key/          # Key entity & repository
      note/         # Note entity, DTO, service, controller
      token/        # JWT token entity & repository
      user/         # User entity, roles, permissions, service, controller
    resources/
      application.yml  # Main configuration
      .env             # Environment variables (e.g., secrets)
  test/
    java/com/notetakingapp/api/
      ...             # Unit/integration tests
Dockerfile
pom.xml
README.md
```

---

## ⚡ Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL (running, with a database named `notetakingapp`)
- (Optional) Docker

### 1. Clone & Configure

```bash
git clone https://github.com/yourusername/notetakingappapi.git
cd notetakingappapi
```

Edit `src/main/resources/application.yml` and `.env` for your database and secrets.

### 2. Database Setup

Create the database and user in PostgreSQL:

```sql
CREATE DATABASE notetakingapp;
CREATE USER notetaking WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE notetakingapp TO notetaking;
```

### 3. Build & Run

#### Locally

```bash
./mvnw spring-boot:run
```

#### With Docker

```bash
docker build -t notetakingappapi .
docker run -p 8012:8012 --env-file src/main/resources/.env notetakingappapi
```

---

## 🔒 Security Model

- **Passwords**: Hashed with BCrypt.
- **Notes**: Encrypted with a per-user AES key.
- **Key Storage**: AES keys are encrypted with RSA, and RSA private keys are layered with AES encryption and salts.
- **JWT**: All endpoints (except `/api/v1/user/auth/**`) require a valid JWT.
- **Role/Permission System**: Fine-grained, extensible.

---

## 📚 API Endpoints

### Authentication

- `POST /api/v1/user/auth/register` — Register a new user.
- `POST /api/v1/user/auth/authenticate` — Login and receive JWT.

### Users

- `GET /api/v1/user` — List all users (admin only).
- `GET /api/v1/user/{id}` — Get user by ID.
- `GET /api/v1/user/email/{email}` — Get user by email.
- `PUT /api/v1/user` — Update user.
- `DELETE /api/v1/user/{id}/delete` — Delete user.

### Notes

- `GET /api/v1/note` — List all notes (admin/manager).
- `GET /api/v1/note/{noteId}` — Get note by ID.
- `GET /api/v1/note/user-id/{userId}` — Get notes for a user.
- `POST /api/v1/note` — Create a note (encrypted).
- `PUT /api/v1/note` — Update a note (encrypted).
- `DELETE /api/v1/note/{id}/delete` — Delete a note.

> **All note content is encrypted at rest and in transit.**

---

## 🛠️ Customization & Extensibility

- **Add new roles/permissions**: Edit `Role.java` and `Permission.java`.
- **Change encryption algorithms**: Update `crypto/AES.java` or `crypto/RSA.java`.
- **Switch database**: Update `application.yml` and dependencies.

---

## 🧪 Testing

Run all tests:

```bash
./mvnw test
```

---

## 🤝 Contributing

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the Apache License 2.0.

---

## 🙋 FAQ

- **How are notes encrypted?**  
  Each note is encrypted with a per-user AES key, which is itself protected by RSA and layered AES encryption.

- **Can I use a different database?**  
  Yes, update the datasource config in `application.yml` and add the appropriate JDBC driver.

- **How do I add more user roles?**  
  Extend `Role.java` and update security config as needed.

---

## 📫 Contact

For questions, suggestions, or support, open an issue or contact the maintainer at [henokeshetu@proton.me].
