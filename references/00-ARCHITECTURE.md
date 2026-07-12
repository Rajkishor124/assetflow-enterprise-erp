# Architecture Rules

These are load-bearing constraints, not suggestions. Every module in every phase
must comply.

## Layering

```
HTTP Request
   → Controller   (binds request, @Valid, calls service, wraps response)
     → Service     (business logic, transactions, @PreAuthorize, orchestration)
       → Repository (Spring Data JPA, query methods, no logic)
         → Database
```

- Controllers never contain business logic — no `if` statements about business
  rules in a controller. If you write `if (asset.getStatus() != AVAILABLE)` in a
  controller, move it to the service.
- Repositories are interfaces only (Spring Data JPA), plus `@Query` for anything
  Spring Data derivation can't express cleanly (overlap checks, aggregates).
- Services own `@Transactional` boundaries. A service method that performs more
  than one repository write is transactional by default.

## DTO pattern

- Entities (`@Entity` classes) never appear in a controller signature — not as a
  `@RequestBody`, not as a return type.
- Every module has:
  - `dto/<Thing>RequestDTO.java` — inbound, Bean Validation annotations
  - `dto/<Thing>ResponseDTO.java` — outbound, only fields the client should see
  - `mapper/<Thing>Mapper.java` — MapStruct interface, `@Mapper(componentModel = "spring")`
- Password hashes, internal audit columns not meant for the client, and other
  entities' full sub-graphs are never included in a response DTO unless the
  endpoint explicitly needs them.

## Standard API response envelope

Every endpoint — success or failure — returns this shape:

```json
{
  "success": true,
  "message": "Asset created successfully",
  "data": { "...": "..." },
  "timestamp": "2026-07-12T10:15:30Z"
}
```

Error example:

```json
{
  "success": false,
  "message": "Asset already allocated to Alice Johnson. Consider a transfer request.",
  "data": null,
  "timestamp": "2026-07-12T10:15:30Z"
}
```

Implement as a generic `ApiResponse<T>` wrapper class plus a
`@RestControllerAdvice` global exception handler that:

- Catches `MethodArgumentNotValidException` → 400, aggregates field errors into
  one readable message.
- Catches custom domain exceptions (e.g. `AssetAlreadyAllocatedException`,
  `InvalidStateTransitionException`, `BookingConflictException`) → 400/409 with
  the exception's message.
- Catches `AccessDeniedException` → 403 with a generic "not authorized" message
  (never leak which specific check failed).
- Catches everything else → 500 with a generic message; full stack trace goes to
  the server log only, never to the client.

## Security architecture

- `SecurityConfig`: stateless session policy, JWT filter chain, CORS restricted to
  the known frontend origin(s), CSRF disabled (stateless JWT API, no cookies used
  for auth).
- `JwtAuthenticationFilter`: extracts `Authorization: Bearer <token>`, validates
  signature + expiry, loads `UserDetails`, sets `SecurityContext`.
- Access token: short-lived (15 min). Refresh token: longer-lived (7 days), stored
  server-side (DB table or cache) so it can be revoked; rotate on use.
- Passwords: BCrypt, strength ≥ 10.
- `@PreAuthorize("hasRole('ASSET_MANAGER')")` (or composite expressions) on every
  service method that isn't universally accessible. Controller-level
  `@PreAuthorize` is a defense-in-depth nicety, not a substitute — the service
  layer is the real boundary because services can be invoked from more than one
  entry point over the project's lifetime.

## Auditing & soft delete

- Every entity extends a shared `BaseEntity`:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    @Enumerated(EnumType.STRING)
    private RecordStatus status = RecordStatus.ACTIVE; // ACTIVE / INACTIVE
}
```

- Enable Spring Data JPA Auditing (`@EnableJpaAuditing`) with an
  `AuditorAware<Long>` bean that pulls the current user id from the security
  context.
- "Delete" endpoints set `status = INACTIVE`, never `DELETE FROM`. All list/find
  queries filter `status = ACTIVE` by default (use `@Where(clause = "status =
  'ACTIVE'")` on the entity, or explicit repository query methods — pick one
  approach per entity and be consistent within that entity).
- Every create/update/status-change/approve/reject action on a domain entity
  writes one row to `activity_logs` (module, action, entity id, actor, details,
  timestamp). Implement this as a lightweight `ActivityLogService.log(...)` called
  from the relevant service methods, or an `@EventListener` on domain events if
  you prefer decoupling — either is acceptable, but it must be consistent and it
  must not be skippable by forgetting to call it in one service (prefer the event
  listener approach specifically because it can't be forgotten).

## Feature-based backend folder structure

```
backend/src/main/java/com/ovidea/assetflow/
├─ auth/
│  ├─ controller/AuthController.java
│  ├─ service/AuthService.java
│  ├─ dto/{SignupRequestDTO, LoginRequestDTO, TokenResponseDTO}.java
│  └─ entity/RefreshToken.java
├─ organization/
│  ├─ controller/{DepartmentController, UserController}.java
│  ├─ service/{DepartmentService, UserService}.java
│  ├─ repository/{DepartmentRepository, UserRepository}.java
│  ├─ entity/{Department, User}.java
│  ├─ dto/...
│  └─ mapper/...
├─ assets/
│  ├─ (categories + assets + documents; see 01-DATABASE-SCHEMA.md)
├─ allocations/
│  ├─ (allocations + transfers + returns)
├─ booking/
│  ├─ (resources + bookings)
├─ maintenance/
│  ├─ (maintenance_requests + maintenance_history)
├─ audits/
│  ├─ (audit_cycles + audit_items)
├─ dashboard/
│  └─ controller/DashboardController.java   (read-only aggregate queries)
├─ reports/
│  └─ (CSV/PDF export)
├─ notifications/
│  └─ (notifications entity + service + listener)
├─ activitylog/
│  └─ (activity_logs entity + service)
│
├─ config/          (SecurityConfig, SwaggerConfig, JpaAuditingConfig, CorsConfig)
├─ security/        (JwtTokenProvider, JwtAuthenticationFilter, UserDetailsServiceImpl)
├─ common/          (BaseEntity, RecordStatus enum, ApiResponse<T>)
├─ exception/       (domain exceptions, GlobalExceptionHandler)
└─ util/            (date/time helpers, pagination helpers)
```

Role/permission source of truth: `roles` table seeded via Flyway
(`ADMIN`, `ASSET_MANAGER`, `DEPT_HEAD`, `EMPLOYEE`). Spring Security roles are
prefixed `ROLE_` internally per Spring convention;
`@PreAuthorize("hasRole('ASSET_MANAGER')")` maps to `ROLE_ASSET_MANAGER`.

## Frontend architecture (Next.js)

Feature-based, not type-based:

```
frontend/src/
├─ app/                      (App Router routes/pages)
│  ├─ (auth)/login/
│  ├─ (dashboard)/dashboard/
│  ├─ assets/
│  ├─ allocations/
│  ├─ bookings/
│  ├─ maintenance/
│  ├─ audits/
│  └─ admin/ (users, departments, roles)
├─ features/
│  ├─ auth/          (api.ts, hooks.ts, types.ts, components/)
│  ├─ assets/
│  ├─ allocations/
│  ├─ bookings/
│  ├─ maintenance/
│  ├─ audits/
│  └─ dashboard/
├─ components/ui/            (shadcn primitives)
├─ lib/
│  ├─ api-client.ts          (Axios instance, interceptors for JWT + refresh)
│  ├─ auth.ts                (token storage/retrieval helpers)
│  └─ validators/            (shared Zod schemas)
└─ types/                    (shared cross-feature types)
```

- Each `features/<name>/` folder owns its API calls (Axios + TanStack Query
  hooks), its Zod validation schema, its TypeScript types, and its
  feature-specific components. Cross-feature imports go through
  `features/<name>/index.ts` public exports only — no reaching into another
  feature's internal files.
- Axios instance in `lib/api-client.ts` has a response interceptor: on 401 with
  an expired-token signal, attempt one silent refresh via
  `/auth/refresh-token`, replay the original request; on refresh failure, clear
  tokens and redirect to `/login`.
- Every form: React Hook Form + a Zod schema from `lib/validators/`, shared
  between create and edit forms where the shape overlaps.
- Server state (anything from the API) lives in TanStack Query, not component
  state or global stores. Client-only UI state (modal open/closed, selected tab)
  can use local `useState`.

## Consistency rules across all modules

- Enum-like statuses are always UPPER_SNAKE_CASE strings, backed by a Java enum,
  persisted with `@Enumerated(EnumType.STRING)` — never persist ordinal ints.
- Every list endpoint supports `?page=&size=` pagination (Spring `Pageable`) and
  returns a paginated envelope inside `data`: `{ content, page, size,
  totalElements, totalPages }`.
- Timestamps are ISO-8601 UTC on the wire.
- Every FK column gets a DB index unless it's already covered by a composite
  index that leads with it (see `01-DATABASE-SCHEMA.md` for the specific index
  list — don't improvise different indexes).
