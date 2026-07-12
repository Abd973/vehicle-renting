# AGENTS.md — Project Conventions & Guidelines

## Project Overview
- **Name:** Vehicle Renting (Peer-to-peer vehicle rental platform for Egypt)
- **Stack:** Spring Boot 4.0.2, Java 17, PostgreSQL, Maven
- **Package:** `com.projects.vehicle_renting`
- **SRS & Diagrams:** `/Docs` directory (PDF files)

## Git Workflow (IMPORTANT — Follow this every time)
1. Always create a **feature branch** for each new feature
2. Make **many small, focused commits** within the branch (NOT one big commit)
3. Each commit should represent **one logical step** (e.g., entity only, then repo only, then service only)
4. **Merge to main** only when the feature is complete and verified
5. Use conventional commit messages: `feat:`, `fix:`, `test:`, `refactor:`

## Development Approach
- **One feature at a time** — do NOT set up all dependencies upfront
- Only add dependencies needed for the **current feature**
- Build **end-to-end** for each feature: entity → repo → DTO → mapper → service → controller → tests
- **Learn as you go** — each feature should teach new concepts
- The user is in the **learning process** — explain what you're doing and why

## Code Conventions
- Use **Lombok** (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- Use **MapStruct** for entity↔DTO mapping
- Use **Bean Validation** (`@Valid`, `@NotBlank`, `@NotNull`, etc.) on DTOs
- Wrap all API responses in **`ApiResponse<T>`** (`{ success, message, data, timestamp }`)
- Use **custom exceptions** (ResourceNotFoundException, BadRequestException, etc.)
- Follow **clean architecture**: Controller → Service → Repository → Entity
- No business logic in controllers

## Feature Roadmap (Incremental Build)
| # | Feature | Status |
|---|---------|--------|
| 1 | Global Exception Handling | DONE |
| 2 | User Entity (full stack) | NEXT |
| 3 | Auth & JWT Security | PENDING |
| 4 | Category Entity | PENDING |
| 5 | Car Entity (search, approval, availability) | PENDING |
| 6 | Booking (double-booking prevention, lifecycle) | PENDING |
| 7 | Wallet & Payment (PaymentGateway pattern) | PENDING |
| 8 | Reviews (both-party feedback) | PENDING |
| 9 | Admin Dashboard & Reports | PENDING |
| 10 | Docker | PENDING |
| 11 | Redis Caching | PENDING |
| 12 | RabbitMQ Async Messaging | PENDING |
| 13 | OpenAPI/Swagger | PENDING |
| 14 | Logging & Monitoring (Actuator) | PENDING |
| 15 | Testing (Testcontainers) | PENDING |
| 16 | AI/RAG Integration (capstone) | PENDING |

## SRS Key Requirements
- **User Roles:** ADMIN, RENTER, OWNER, COMPANY
- **Wallet System:** Simulated payments — users have wallet balances, atomic transfers
- **PaymentGateway interface:** Swappable implementation (SimulatedGateway now, real later)
- **Car Approval:** Owner submits → Admin approves
- **Availability:** Simple active/inactive boolean on cars
- **Reviews:** Both parties leave feedback after completed rental
- **Double Booking:** Prevent overlapping active bookings via date conflict detection
- **VIN:** Unique, 17 characters
- **AI/RAG:** Future scope — store structured data to support AI querying

## When Starting a New Session
1. Read this AGENTS.md first
2. Check `git log --oneline -10` to see current progress
3. Check which feature is "NEXT" in the roadmap
4. Create a feature branch for it
5. Follow the small-commits workflow
