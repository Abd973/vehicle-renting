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
| 3 | Auth & JWT Security | DONE |
| 4 | Brand Entity | DONE |
| 5 | Car Entity (search, approval, availability) | NEXT |
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

## Current State & Design Decisions (as of Auth & JWT feature)
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
- **Auth & JWT (Feature 3) is DONE, merged to main** via feature/login-jwt. This replaced the older
  plan where login was split across `AuthController` + `UserController` with `UserResponse` DTOs.
- **Package reorganization:** all auth/security classes live in `security/` package (replacing an old
  `config/SecurityConfig` and `controller/AuthController`):
  - `security/SecurityConfig.java` — SecurityFilterChain (stateless, CSRF off), `PasswordEncoder`,
    `AuthenticationManager` bean, permits `/api/auth/register` + `/api/auth/login`, filters.
  - `security/JwtService.java` — pure utility: `generateToken`, `extractEmail`, `isTokenValid`,
    expiry check. Reads `jwt.secret` + `jwt.expiration` from config via `@Value` (non-final fields;
    **`@Value` cannot inject into `final` fields**). Suspicious base64 string must sit on ONE line.
  - `security/CustomUserDetailsService.java` — implements `UserDetailsService`, wraps `User` entity
    into Spring Security's `User` with `ROLE_` + `role.name()` authority. Bridges the domain entity
    and Security (Option B; do NOT make `User` implement `UserDetails`).
  - `security/JwtAuthenticationFilter.java` — `OncePerRequestFilter`; reads `Authorization: Bearer`,
    validates JWT, loads user, sets `SecurityContextHolder`. Wired via
    `addFilterBefore(... , UsernamePasswordAuthenticationFilter.class)`.
  - `security/AuthenticationController.java` — `POST /api/auth/register`, `POST /api/auth/login`.
  - `security/AuthenticationService.java` — `register()` (now returns a token = auto-login) and
    `login()` (uses `AuthenticationManager`; catches `BadCredentialsException` → `UnauthorizedException`).
  - `security/AuthenticationResponse.java` — `{ String token }`, returned by both register + login.
- **DTOs:** register auto-login means only a token is returned. `LoginResponse` +
  `UserResponse` were dropped; `AuthenticationResponse` replaced them. `UserResponse` was later
  re-added (deviation) for the `GET /api/users/me` profile endpoint.
- **Current user endpoint:** `GET /api/users/me` in `controller/UserController` reads the email from
  `SecurityContextHolder`, then `service/UserService.getCurrentUser(email)` builds a `UserResponse`.
  Guards: no/invalid/expired token → 403; valid token → user profile.
- **JWT config:** `jwt.secret: ${JWT_SECRET}` (NO hardcoded default — must come from env) and
  `jwt.expiration: ${JWT_EXPIRATION:86400000}` (24h). The secret MUST live only in the gitignored
  `.env`/`.envrc`; never commit it. If it is ever exposed, rotate it (`openssl rand -base64 64`).
- **Env vars / direnv:** the app uses `direnv` (`.envrc`) to load `DB_*` + `JWT_SECRET` +
  `JWT_EXPIRATION`. After EDITING `.envrc`, run `direnv allow .` again. If direct shell use, run
  `eval "$(direnv export bash)"`. A bare `./mvnw spring-boot:run` without env fails to boot
  (`Could not resolve placeholder 'JWT_SECRET'`), which is intentional (fail fast if JWT not set).
- **Known gotcha:** after moving/removing a `@Configuration` class, run `./mvnw clean` before
  `spring-boot:run` — stale classes in `target/` cause `ConflictingBeanDefinitionException`.
- **Registration is RENTER/OWNER/COMPANY self-selectable; ADMIN is admin-granted only.**
- **Password policy:** min 8 chars + regex requiring upper/lower/digit/special; `confirmPassword` +
  `@AssertTrue isPasswordConfirmed()`.
- **Validation pattern:** optional/enum fields validated via `@AssertTrue` methods inside DTOs (simple,
  learning-friendly) rather than reusable class-level validators.
- **Product discovery model (from a Rhoda-like Android reference app):** the home page is built around
  **location availability**, **popular brands**, **browse by destination (Egypt governorates)**, and
  **popular cars** — NOT a generic "category" taxonomy. So the roadmap's generic `Category` feature was
  **swapped for `Brand`** (Feature 4). Governates/location + profiles will be separate concerns later.
- **Profiles deferred** (renter/host onboarding: profile picture, National ID/passport upload, driving
  license, proof of residence, payout method) — captured but NOT being built now (would touch the deferred
  `OwnerProfile`/`CompanyProfile` plan). To be revisited when we add owner/company profiles.
- **Admin account:** `/register` rejects `ADMIN` (admin-granted only). A dev-time seeding mechanism exists:
  `config/AdminSeeder.java` — a `CommandLineRunner` that creates an admin if none exists. Uses
  `admin.email`/`admin.password` config (defaults `admin@vehicle-renting.com` / `Admin@1234`). No
  `@Profile` guard yet (no profiles exist in the project; revisit when Docker/profiles land).
- **Brand feature (Feature 4) is DONE, merged to main** via feature/brand. Swapped out the generic
  `Category` for `Brand` per the product discovery model. Uses a single `BrandDTO` for both request and
  response (id/createdAt/updatedAt populated only on response). CRUD endpoints under `/api/brands`;
  write ops guarded with `@PreAuthorize("hasRole('ADMIN')")`. Enabled `@EnableMethodSecurity` in
  `SecurityConfig` for method-level authorization. DB unique constraint on `brands.name` +
  service-level `existsByName` check (service → `ConflictException`, DB → backstop).
- **AccessDenied handling:** `@PreAuthorize` failures throw Spring's `AccessDeniedException`, which is NOT
  `ForbiddenException`. `GlobalExceptionHandler` maps it to 403 (message: "Access denied: this action
  requires ADMIN role"). Generic `@ExceptionHandler(Exception.class)` catches anything unhandled → 500.

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
5. Check `git stash list` — there may be WIP stashed that belongs to the current/next feature;
   `git stash pop` to recover if relevant
6. Follow the small-commits workflow
