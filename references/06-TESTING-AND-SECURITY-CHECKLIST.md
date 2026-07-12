# Testing, Security & Deployment Checklist

Use this as the "definition of done" gate for Phases 15–19, and as prep
material for judge Q&A.

## Testing checklist

- [ ] Unit tests (JUnit + Mockito) cover: double-allocation rejection, booking
      overlap rejection (including the adjacent-booking-allowed case), every
      invalid asset state transition, every invalid maintenance state
      transition, RBAC boundary for at least allocate / transfer-approve /
      user-role-change.
- [ ] Integration tests (`@SpringBootTest`, Testcontainers or H2) cover at
      least: signup → login → access protected route; allocate → attempt
      double-allocate (expect 409) → return → reallocate succeeds; create
      booking → attempt overlapping booking (expect 409) → create adjacent
      booking (expect success).
- [ ] Concurrency test: fire two simultaneous allocation requests for the same
      asset (e.g. via parallel threads or an executor in the test) and assert
      exactly one succeeds. Same for simultaneous overlapping bookings.
- [ ] Frontend: Jest/RTL tests on the asset-creation form and booking-creation
      form validation logic (Zod schema + submit behavior).
- [ ] Build is configured to fail if any test fails — verify this is actually
      true, not just assumed (run a deliberately failing test and confirm the
      build reports failure).

## Security checklist (OWASP-aligned)

- [ ] Passwords hashed with BCrypt, never logged, never returned in any DTO.
- [ ] JWT secret and DB credentials come from environment variables, never
      committed to the repo.
- [ ] Access tokens short-lived (≤15 min); refresh tokens stored hashed and
      revocable.
- [ ] All inputs validated server-side via Bean Validation, regardless of
      client-side Zod validation (client validation is UX, not security).
- [ ] All queries use JPA/parameterized access — no string-concatenated SQL
      anywhere in the codebase (grep for `"SELECT"` string concatenation as a
      final check).
- [ ] CORS restricted to the actual frontend origin(s), not `*`.
- [ ] Security headers set (HSTS, X-Content-Type-Options: nosniff, a baseline
      CSP) via Spring Security config.
- [ ] `@PreAuthorize` present on every service method that isn't intentionally
      open to all authenticated users — spot-check by listing all `public`
      service methods and confirming each one is deliberately unguarded or
      guarded.
- [ ] No stack traces or internal exception messages ever reach an HTTP
      response body — confirm by forcing a 500 in a test and inspecting the
      actual response.
- [ ] `activity_logs` has no update or delete code path anywhere, including
      for Admin.

## Deployment checklist

- [ ] `docker-compose up` from a clean clone brings up Postgres, backend,
      frontend with no manual steps beyond copying an `.env.example`.
- [ ] Flyway migrations run automatically on backend startup against a fresh
      DB.
- [ ] README documents every required environment variable.
- [ ] Swagger UI (`/swagger-ui.html`) is reachable and every endpoint listed in
      `03-API-CONTRACT.md` is documented there accurately.

## Likely judge questions & how to answer them

**Q: Why PostgreSQL instead of Firebase/Supabase/MongoDB?**
A: The system models highly relational, transactional data — allocations,
transfers, and bookings all depend on referential integrity and constraint
enforcement (unique active-allocation-per-asset, overlap prevention) that a
relational schema with real foreign keys and partial unique indexes expresses
directly. A document store would push that integrity logic entirely into
application code with weaker guarantees.

**Q: How did you ensure normalization?**
A: ~18 tables, each representing one entity or one relationship history
(allocations, transfers, and returns are separate tables rather than mutable
fields on `assets`, so full history is preserved without duplication). Foreign
keys enforce integrity throughout; see the ER diagram in
`01-DATABASE-SCHEMA.md`.

**Q: Why DTOs and MapStruct?**
A: Entities carry internal fields (password hashes, audit columns, lazy
associations) that shouldn't cross the wire. DTOs decouple the API contract
from the persistence model and let each evolve independently; MapStruct
generates the mapping code at compile time, removing manual mapping bugs.

**Q: How do you prevent double-allocation under concurrent requests?**
A: A pessimistic row lock (`SELECT ... FOR UPDATE`) on the asset row inside the
transaction, plus a partial unique index
(`WHERE status = 'ACTIVE'`) on `allocations(asset_id)` as a database-level
backstop that holds even if the lock were somehow bypassed.

**Q: How do you prevent overlapping bookings?**
A: A half-open-interval overlap query (`start_time < newEnd AND end_time >
newStart`) run inside the same transaction as the insert, backed by a
composite index on `(resource_id, start_time, end_time)` for performance, with
an advisory lock scoped to the resource for correctness under concurrency.

**Q: Why JWT instead of server sessions?**
A: The API is stateless by design to pair with a Next.js SPA-style frontend;
JWT avoids server-side session storage and scales horizontally without sticky
sessions. Refresh tokens (stored hashed, revocable) offset JWT's usual
revocation weakness.

**Q: How would this scale beyond the hackathon?**
A: Stateless services scale horizontally behind a load balancer. PostgreSQL
can add read replicas for read-heavy endpoints (dashboard, reports). Reference
data (roles, categories) is a natural caching candidate. The feature-based
module boundaries map cleanly onto a future microservice split if that's ever
warranted.

**Q: What would you add with more time?**
A: Redis caching for dashboard aggregates, WebSocket-based real-time
notifications instead of polling, a full CI/CD pipeline, load testing against
a realistic multi-thousand-asset dataset, and a `TECHNICIAN` role distinct from
`ASSET_MANAGER` for maintenance-only staff.
