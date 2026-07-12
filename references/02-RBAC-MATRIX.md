# RBAC — Roles & Permission Matrix

## Roles (seeded via Flyway, table `roles`)

| Role code       | Spring authority       | Description |
|-----------------|-------------------------|--------------|
| `ADMIN`         | `ROLE_ADMIN`            | Full system control: users, departments, roles, all data |
| `ASSET_MANAGER` | `ROLE_ASSET_MANAGER`    | Owns asset lifecycle, allocations, bookings, maintenance, audits |
| `DEPT_HEAD`     | `ROLE_DEPT_HEAD`        | Manages their own department's people and approves their dept's requests |
| `EMPLOYEE`      | `ROLE_EMPLOYEE`         | Requests things for themselves; views their own data |

## Permission matrix

| Capability                                   | Admin | Asset Manager | Dept Head (own dept) | Employee (own records) |
|-----------------------------------------------|:-----:|:--------------:|:---------------------:|:------------------------:|
| Create/edit/delete departments                | ✅ | ❌ | ❌ | ❌ |
| Create users, assign roles                    | ✅ | ❌ | ❌ | ❌ |
| View all users                                 | ✅ | ✅ (view) | ✅ (dept only) | ❌ |
| Create/edit asset categories                   | ✅ | ✅ | ❌ | ❌ |
| Create/edit assets                             | ✅ | ✅ | ❌ | ❌ |
| View assets                                    | ✅ | ✅ | ✅ (dept assets) | ✅ (assigned to them) |
| Allocate an asset                              | ✅ | ✅ | ❌ | ❌ |
| Request a transfer                             | ✅ | ✅ | ✅ (dept) | ✅ (self, request only) |
| Approve a transfer                             | ✅ | ✅ | ✅ (dept) | ❌ |
| Request a return                               | ✅ | ✅ | ✅ (dept) | ✅ (self) |
| Approve a return                               | ✅ | ✅ | ✅ (dept) | ❌ |
| Create/manage resources (rooms etc.)           | ✅ | ✅ | ❌ | ❌ |
| Book a resource                                | ✅ | ✅ | ✅ | ✅ (self) |
| Cancel a booking                                | ✅ | ✅ | ✅ (dept) | ✅ (own booking) |
| Create maintenance request                     | ✅ | ✅ | ✅ | ✅ (self) |
| Approve/assign maintenance request              | ✅ | ✅ | ✅ (dept) | ❌ |
| Update maintenance status                      | ✅ | ✅ | ❌ | ❌ |
| Start/close audit cycle                        | ✅ | ✅ | ❌ | ❌ |
| Submit audit item result (as assigned auditor) | ✅ | ✅ | ✅ (if assigned) | ✅ (if assigned) |
| View audit results                             | ✅ | ✅ | ✅ (dept) | ❌ (report-only, no dedicated view) |
| View dashboard summary                          | ✅ | ✅ | ✅ (dept-scoped) | ❌ |
| Generate/download reports                       | ✅ | ✅ | ❌ | ❌ |
| View activity log                              | ✅ | ❌ | ❌ | ❌ |

## Enforcement rules

1. **Every non-Employee-self-service endpoint gets an explicit
   `@PreAuthorize`** at the service method — not just the controller. Examples:

   ```java
   @PreAuthorize("hasAnyRole('ADMIN','ASSET_MANAGER')")
   public AssetResponseDTO createAsset(AssetRequestDTO dto) { ... }

   @PreAuthorize("hasRole('ADMIN')")
   public void changeUserRole(Long userId, String newRole) { ... }
   ```

2. **"Own dept" and "own records" scoping is not expressible in `@PreAuthorize`
   alone** — it requires a service-layer check against the authenticated
   principal's `dept_id` or `user_id` after the role check passes. Pattern:

   ```java
   @PreAuthorize("hasAnyRole('ADMIN','ASSET_MANAGER','DEPT_HEAD')")
   public void approveTransfer(Long transferId, Long approverId) {
       Transfer t = transferRepository.findById(transferId).orElseThrow(...);
       User approver = userRepository.findById(approverId).orElseThrow(...);
       if (approver.hasRole("DEPT_HEAD")
           && !departmentScopeMatches(approver, t)) {
           throw new AccessDeniedException("Not authorized for this department");
       }
       // ... proceed
   }
   ```

   Do not try to cram dept-scoping into a SpEL `@PreAuthorize` expression beyond
   the role check — it becomes unreadable and untestable. Role check in the
   annotation, scope check in the method body.

3. **Employee self-service** ("own records") means: the service checks
   `resource.userId == currentUser.id` (or `requestedBy`, or `assetHolder`,
   depending on the entity) before allowing the action, in addition to the role
   check that lets Employees hit the endpoint at all.

4. **403 vs 404**: when an Employee requests an action on someone else's record
   they have no visibility into, prefer returning 403 (not 404) — this is a
   hackathon demo where showing correct authorization behavior matters more than
   hiding resource existence, and it's simpler to reason about and test.

5. Anonymous/unauthenticated requests to any endpoint other than
   `/auth/signup`, `/auth/login`, `/auth/refresh-token`, and
   `/swagger-ui/**`/`/v3/api-docs/**` are rejected with 401 by the JWT filter
   chain before reaching a controller.
