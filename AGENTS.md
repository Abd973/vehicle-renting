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

## Learning-First Approach (CRITICAL — This overrides "just build it" instincts)

The user is learning Spring Boot through this project. Your role is to be a
**thinking partner**, not an autonomous builder. Follow this cycle for every feature:

### The Workflow
1. **User proposes a rough design** (even if incomplete or wrong)
2. **You critique it** — suggest improvements, point out missing pieces, explain tradeoffs
3. **User refines the design** based on your feedback
4. **You implement ONE small step** (e.g., just the entity, just the repo)
5. **User reads the code and asks questions** — "why this annotation?", "what if I remove this?"
6. **You explain the reasoning** — not just what the code does, but why it exists
7. **Repeat from step 4** until the feature is complete

### Rules for the AI
- **NEVER produce code faster than the user can understand it** — slow down, explain
- **ALWAYS explain "why" before "what"** — the design decision matters more than the syntax
- **Ask the user to propose designs first** before you suggest one
- **Encourage experimentation** — "what happens if you remove this?" "what if two users register with the same email?"
- **If the user asks "just build it"** — remind them of this approach, but respect their choice
- **After writing code, offer to explain it line by line** — don't assume they understand

### What NOT to do
- Don't dump a full feature in one step — break it into small commits
- Don't add dependencies the user didn't ask for
- Don't skip the "why" — every annotation, every pattern, every design choice should be explained
- Don't treat the user as a product owner — they are a developer who wants to learn

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
| 2 | User Entity (full stack) | DONE |
| 3 | Auth & JWT Security | NEXT |
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

## Current State & Design Decisions (as of the email-identity refactor)
- **User Entity (Feature 2) is DONE, merged to main** via feature/register + feature/register-roles.
- **Email is the sole user identity** — `username` was removed from the codebase AND dropped from the
  `users` DB column in the `refactor: replace username with email as user identity` commit (branch
  `refactor/email-identity`, merged).
- **Role selection at registration:** `RegisterRequest` has an optional `role` field (enum `Role`),
  defaulting to `RENTER`. `ADMIN` is rejected via an `@AssertTrue` method in the DTO
  (`isRoleValid()`). Invalid role strings fail JSON-enum deserialization.
- **One role per account** (flat `role` enum on `users`). Plan for later (Option B): separate
  `OwnerProfile` / `CompanyProfile` tables referencing `users` 1:1 — **do NOT add owner/company
  columns to `users` now** to keep that migration easy.
- **Auth flow is split:** registration in `AuthController` (`POST /api/auth/register`), `UserController`
  reserved for profile/account later. `LoginRequest`/`LoginResponse` DTOs already exist but are NOT
  wired up (login is part of Feature 3).
- **Registration is RENTER/OWNER/COMPANY self-selectable; ADMIN is admin-granted only.**
- **Password policy:** min 8 chars + regex requiring upper/lower/digit/special; `confirmPassword` +
  `@AssertTrue isPasswordConfirmed()`.
- **Validation pattern:** optional/enum fields validated via `@AssertTrue` methods inside DTOs (simple,
  learning-friendly) rather than reusable class-level validators.

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
4. Create a feature branch for the next feature
5. Check `git stash list` — there may be WIP stashed (e.g., JWT deps in `pom.xml`, `LoginResponse`)
   that belong to the current/next feature; `git stash pop` to recover if relevant
6. Follow the small-commits workflow
