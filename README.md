# Vehicle Renting

A peer-to-peer vehicle rental platform for Egypt — where owners list cars for rent and renters book them. Built with Spring Boot, secured with JWT, and designed around a clean layered architecture.

## Overview

Vehicle Renting provides the backend API for a car-hire marketplace. Users self-select their account type (RENTER / OWNER / COMPANY) at registration, and an ADMIN role manages catalog data. Car discovery is built around brand and location, mirroring popular Egyptian rental apps.

## Features

### Implemented

- **Authentication & Authorization (JWT)**
  - Register (auto-login: returns a token) and Login
  - Stateless JWT security via a custom `OncePerRequestFilter`
  - BCrypt password hashing
  - Role-based access control (`ADMIN` / `RENTER` / `OWNER` / `COMPANY`)
  - `GET /api/users/me` — current user profile from the token
- **Brand management**
  - Full CRUD under `/api/brands`
  - Read endpoints for any authenticated user
  - Create / update / delete restricted to `ADMIN`
  - Duplicate name prevention (service check + DB unique constraint)

### Planned (roadmap)

- Car entity with search, owner-approval workflow, and availability
- Booking with double-booking prevention
- Wallet & simulated payments (swappable `PaymentGateway`)
- Both-party reviews after completed rentals
- Admin dashboard & reports
- Docker, Redis caching, RabbitMQ, OpenAPI/Swagger, Actuator, Testcontainers
- AI/RAG integration (capstone)

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 4.0.2 |
| Web | Spring MVC (REST API) |
| Security | Spring Security, JWT (jjwt 0.13.0), BCrypt |
| Persistence | Spring Data JPA (Hibernate), PostgreSQL |
| Validation | Bean Validation (Jakarta Validation) |
| Build | Maven |
| Utilities | Lombok, MapStruct |
| Configuration | Environment variables via direnv (`.envrc`) |

## Architecture

Clean layered architecture with a strict dependency direction:

```
Controller → Service → Repository → Entity
```

- **Controllers** — HTTP concerns only (no business logic)
- **Services** — business rules, validation, exceptions
- **Repositories** — Spring Data JPA data access
- **`security/` package** — `SecurityConfig`, `JwtService`, `JwtAuthenticationFilter`, `CustomUserDetailsService`, `AuthenticationController`/`AuthenticationService`
- **Responses** — unified `ApiResponse<T>` envelope: `{ success, message, data, timestamp }`
- **Errors** — custom exceptions (`ResourceNotFoundException`, `ConflictException`, `UnauthorizedException`, `ForbiddenException`, `BadRequestException`) handled centrally via `@RestControllerAdvice`

## Getting Started

### Prerequisites

- JDK 17
- Maven (or use the wrapper `./mvnw`)
- PostgreSQL running locally

### Setup

1. **Create the database** (default: `vehicle_renting`):
   ```sql
   CREATE DATABASE vehicle_renting;
   ```

2. **Configure environment variables** using direnv. Copy `env.example` to `.envrc` and fill in values:
   ```bash
   DB_URL=jdbc:postgresql://localhost:5432/vehicle_renting
   DB_USERNAME=postgres
   DB_PASSWORD=your_password_here
   JWT_SECRET=your_jwt_secret_here        # generate: openssl rand -base64 64
   JWT_EXPIRATION=86400000                # 24 hours in ms
   ```
   Then `direnv allow`.

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. The app starts on `http://localhost:8080`.

> Note: `JWT_SECRET` has no default in `application.yaml` — the app fails fast at startup if it is not set. Never commit the real secret.

### Admin account

For development, `config/AdminSeeder.java` creates an admin on startup if none exists (defaults: `admin@vehicle-renting.com`). Configure via `admin.email` / `admin.password`. This is dev tooling — replace or remove it for production.

## API Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/auth/register` | Public | Register (returns token = auto-login) |
| POST | `/api/auth/login` | Public | Login (returns token) |
| GET | `/api/users/me` | Authenticated | Current user profile |
| GET | `/api/brands` | Authenticated | List all brands |
| GET | `/api/brands/{id}` | Authenticated | Get one brand |
| POST | `/api/brands` | ADMIN | Create brand |
| PUT | `/api/brands/{id}` | ADMIN | Update brand |
| DELETE | `/api/brands/{id}` | ADMIN | Delete brand |

## Configuration Reference

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/vehicle_renting` | PostgreSQL JDBC URL |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | *(empty)* | Database password |
| `JWT_SECRET` | *(none — required)* | Base64 signing key (`openssl rand -base64 64`) |
| `JWT_EXPIRATION` | `86400000` | Token lifetime in milliseconds (24h) |

## Documentation

The full Software Requirements Specification and diagrams live in `/Docs` (PDF).

## License

This project is for learning purposes and has no license yet.