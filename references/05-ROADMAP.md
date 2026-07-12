# Phased Build Roadmap

Work top to bottom. Do not start a phase until the previous phase's acceptance
criteria are met. Each phase references the exact schema/API/rules docs it
needs — re-read the relevant section before implementing, don't rely on memory
of it from earlier in the session.

---

### Phase 0 — Requirements & System Design
**Deliverables:** Confirm the ER diagram (`01-DATABASE-SCHEMA.md`), API catalog
(`03-API-CONTRACT.md`), and role matrix (`02-RBAC-MATRIX.md`) are internally
consistent — they're already written, this phase is a verification pass, not a
from-scratch design exercise.
**Acceptance:** You can explain, in your own words, every table's purpose and
every role's boundaries before writing code.

### Phase 1 — Project Setup & Configuration
**Deliverables:** Initialized Spring Boot + Next.js projects, Docker Compose for
local dev.
**Tasks:**
- Spring Boot 3.x project, Java 21, dependencies: Spring Web, Spring Security,
  Spring Data JPA, PostgreSQL driver, Flyway, Lombok, MapStruct, springdoc-openapi.
- `application.yml` with profiles (`local`, `docker`) — DB URL, JWT secret, token
  expiry as environment variables, never hardcoded.
- Flyway baseline migration + `V2__seed_roles.sql` seeding the four roles.
- `docker-compose.yml`: `postgres`, `backend`, `frontend` services.
- Next.js 15 (App Router) + TypeScript + Tailwind + shadcn/ui init. ESLint +
  Prettier configured.
**Acceptance:** `docker-compose up` brings up Postgres + a Spring Boot app that
starts cleanly with Swagger UI reachable at `/swagger-ui.html`. Next.js dev
server runs with a placeholder login page. Git repo initialized with a
`.gitignore` covering both stacks.

### Phase 2 — Authentication Module
**Refs:** `01-DATABASE-SCHEMA.md` (`users`, `refresh_tokens`),
`03-API-CONTRACT.md` (`/auth/*`), `00-ARCHITECTURE.md` (security architecture).
**Tasks:**
- `POST /auth/signup`, `POST /auth/login`, `POST /auth/refresh-token`,
  `POST /auth/logout`.
- BCrypt password hashing.
- `JwtTokenProvider` (sign/verify/parse), `JwtAuthenticationFilter`.
- Refresh tokens persisted (hashed) in `refresh_tokens`, rotated on use, revoked
  on logout.
**Acceptance:** Signup creates an `EMPLOYEE`-role user. Login returns a valid
JWT that authorizes a protected test endpoint. Expired/invalid tokens are
rejected with 401. Refresh rotates the token and the old one is unusable after.

### Phase 3 — Organization Module
**Refs:** `01-DATABASE-SCHEMA.md` (`departments`, `users`),
`03-API-CONTRACT.md` (`/users`, `/departments`).
**Tasks:**
- `Department` CRUD, self-referential hierarchy, cycle prevention (see
  `04-BUSINESS-RULES.md`).
- `User` read/update endpoints, dept assignment.
**Acceptance:** Departments nest correctly; assigning a department as its own
ancestor is rejected. Deleting a department with active users or child
departments returns 409. Users can be assigned to departments.

### Phase 4 — Role Management & RBAC
**Refs:** `02-RBAC-MATRIX.md` in full.
**Tasks:**
- `PUT /users/{id}/role`, Admin-only.
- `@PreAuthorize` applied to every service method per the matrix.
- Dept-scoping checks (service-layer, not SpEL) for `DEPT_HEAD` actions.
**Acceptance:** Manually verify every row of the permission matrix — write an
integration test per role per module boundary at minimum for allocate,
transfer-approve, and user-role-change, since those are the highest-stakes
checks.

### Phase 5 — Asset Categories Module
**Refs:** `01-DATABASE-SCHEMA.md` (`asset_categories`),
`03-API-CONTRACT.md` (`/asset-categories`).
**Acceptance:** CRUD works; duplicate name rejected 409; delete blocked 409 if
any asset uses the category.

### Phase 6 — Asset Module
**Refs:** `01-DATABASE-SCHEMA.md` (`assets`, `asset_documents`),
`04-BUSINESS-RULES.md` (asset lifecycle state machine),
`03-API-CONTRACT.md` (`/assets`).
**Tasks:** CRUD, search/filter with pagination, document upload, lifecycle
enforcement via the state-machine allow-list.
**Acceptance:** New assets default to `AVAILABLE`. Editing/deleting a non-
`AVAILABLE` asset is rejected 409. Invalid status transitions return 400 with
an explanatory message. Search + pagination return correct, correctly-paged
results.

### Phase 7 — Allocation Module
**Refs:** `01-DATABASE-SCHEMA.md` (`allocations`, `transfers`, `returns`),
`04-BUSINESS-RULES.md` (double-allocation prevention, transfer workflow, return
workflow, concurrency), `03-API-CONTRACT.md`.
**Acceptance:** Allocating an unavailable asset returns 409 with the current
holder's name. Concurrent simultaneous allocation attempts on the same asset:
exactly one succeeds (write a test that fires both in parallel and asserts
this). Transfer approval correctly closes the old allocation and opens a new
one, asset status stays `ALLOCATED` throughout. Return marks asset `AVAILABLE`.

### Phase 8 — Resource Booking Module
**Refs:** `01-DATABASE-SCHEMA.md` (`resources`, `bookings`),
`04-BUSINESS-RULES.md` (booking overlap prevention), `03-API-CONTRACT.md`.
**Acceptance:** Overlapping booking rejected 409. Adjacent booking (end ==
start) succeeds. Cancelling excludes a booking from future overlap checks.
Concurrent overlapping booking attempts: exactly one succeeds (parallel test).

### Phase 9 — Maintenance Module
**Refs:** `01-DATABASE-SCHEMA.md` (`maintenance_requests`,
`maintenance_history`), `04-BUSINESS-RULES.md` (maintenance workflow).
**Acceptance:** Status transitions follow the linear order strictly; skipping a
step (e.g. `PENDING → RESOLVED`) returns 400. Approving syncs asset to
`UNDER_MAINTENANCE`; resolving syncs it back to `AVAILABLE`. Every transition
recorded in `maintenance_history`.

### Phase 10 — Audit Module
**Refs:** `01-DATABASE-SCHEMA.md` (`audit_cycles`, `audit_items`),
`04-BUSINESS-RULES.md` (audit workflow).
**Acceptance:** Only the assigned auditor can submit a given item's result.
Closing a cycle with pending items is rejected (per the documented policy).
Closed cycles are fully read-only — verified by a test that attempts (and
fails) to update a closed cycle's item.

### Phase 11 — Dashboard Module
**Refs:** `03-API-CONTRACT.md` (`/dashboard/*`).
**Tasks:** Aggregate `GROUP BY` queries for summary counts and chart data,
role/dept-scoped.
**Acceptance:** Counts match ground truth in the DB after seeding test data.
Dept Head sees only their department's numbers.

### Phase 12 — Reports Module
**Refs:** `03-API-CONTRACT.md` (`/reports/*`).
**Tasks:** CSV export (simple writer) and PDF export (iText or similar) for
assets, maintenance log, audit results.
**Acceptance:** Downloaded file content matches current DB state exactly.

### Phase 13 — Notification Module
**Refs:** `01-DATABASE-SCHEMA.md` (`notifications`), `03-API-CONTRACT.md`.
**Tasks:** Fire notifications on: allocation created, transfer approved,
maintenance approved/resolved, booking created/cancelled, audit item assigned.
Prefer an `@EventListener` on domain events (published from the relevant
services) over inline calls scattered through every service method, so adding a
new notification trigger later doesn't require touching unrelated modules.
**Acceptance:** Each listed event produces exactly one notification for the
correct recipient. Unread count is accurate; marking read clears it.

### Phase 14 — Activity Log Module
**Refs:** `01-DATABASE-SCHEMA.md` (`activity_logs`),
`00-ARCHITECTURE.md` (auditing section).
**Acceptance:** Every create/update/delete/approve/reject across every module
produces a log row. No update/delete path exists for the log table itself,
including for Admin. `GET /activity-log` filters correctly by user and date
range.

### Phase 15 — Security Enhancements
**Refs:** `06-TESTING-AND-SECURITY-CHECKLIST.md`.
**Tasks:** CORS restricted to the known frontend origin. Security headers
(HSTS, X-Content-Type-Options, CSP baseline) via Spring Security. Confirm no
SQL injection surface (JPA parameter binding everywhere — audit any raw
`@Query` with string concatenation). Optional: rate limiting on `/auth/login`
and `/auth/signup` if time permits (simple in-memory bucket is acceptable for a
hackathon demo — document the limitation if you skip a distributed solution).
**Acceptance:** Checklist in `06-TESTING-AND-SECURITY-CHECKLIST.md` fully
passes.

### Phase 16 — Validation & Exception Handling
**Refs:** `04-BUSINESS-RULES.md` (validation conventions),
`00-ARCHITECTURE.md` (global exception handler).
**Acceptance:** Every request DTO has Bean Validation. No stack trace ever
reaches a client response (verify by forcing a 500 and inspecting the actual
HTTP body). Validation errors return one aggregated, readable message.

### Phase 17 — Performance & Optimization
**Tasks:** Confirm every index listed in `01-DATABASE-SCHEMA.md` exists.
Pagination on every list endpoint. `@EntityGraph` or explicit join-fetch on any
relation prone to N+1 (asset→category, allocation→user, booking→resource are
the likely offenders). `@Cacheable` on read-heavy reference data (`roles`,
`asset_categories`) if time permits.
**Acceptance:** No N+1 query pattern in server logs for the core listing
endpoints (verify with SQL logging turned on during a manual pass, or a
Hibernate statistics check in a test). List endpoints respond well within a
demo-reasonable threshold against a seeded dataset of a few hundred rows per
table.

### Phase 18 — Testing & Quality Assurance
**Refs:** `06-TESTING-AND-SECURITY-CHECKLIST.md`.
**Tasks:** JUnit + Mockito unit tests for service-layer business rules
(double-allocation, booking overlap, invalid state transitions, RBAC boundary
checks). `@SpringBootTest` integration tests for the critical end-to-end flows
(login → allocate → return; create booking → conflicting booking rejected).
Frontend: Jest/RTL tests for the highest-risk forms (asset creation, booking
creation).
**Acceptance:** All tests pass; build fails on test failure (wire into
whatever CI or local script serves as the build gate). Every business rule in
`04-BUSINESS-RULES.md` has at least one test asserting it.

### Phase 19 — Documentation & Final Demo Prep
**Refs:** `07-DEMO-SCRIPT.md`.
**Tasks:** README (setup, env vars, run commands), architecture doc (link back
to these `docs/` files or summarize inline), Swagger UI verified accurate,
practice the full demo script end-to-end at least twice.
**Acceptance:** A new developer can clone the repo and get the full stack
running by following the README alone. The demo script runs live without
errors or improvisation.
