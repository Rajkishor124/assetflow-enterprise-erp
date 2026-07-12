# API Contract

Base path: `/api/v1`. All responses wrapped in the standard envelope described in
`00-ARCHITECTURE.md`. All list endpoints accept `?page=0&size=20` and return the
paginated envelope shown there. Auth: `Authorization: Bearer <accessToken>` on
every endpoint except those listed as public.

## Auth (`/auth`) — public

| Method | Path | Body | Notes |
|---|---|---|---|
| POST | `/auth/signup` | `{ name, email, password }` | Creates user with default role `EMPLOYEE`. Admin promotes later. |
| POST | `/auth/login` | `{ email, password }` | Returns `{ accessToken, refreshToken }` |
| POST | `/auth/refresh-token` | `{ refreshToken }` | Returns new `{ accessToken, refreshToken }`, rotates the stored token |
| POST | `/auth/logout` | `{ refreshToken }` | Revokes the refresh token |

## Users & Departments (`/users`, `/departments`)

| Method | Path | Body | Roles |
|---|---|---|---|
| GET | `/users` | — (query: `role`, `deptId`) | ADMIN, ASSET_MANAGER (view), DEPT_HEAD (dept-scoped) |
| GET | `/users/{id}` | — | ADMIN, self, or same-dept DEPT_HEAD |
| PUT | `/users/{id}/role` | `{ role: "ASSET_MANAGER" }` | ADMIN only |
| PUT | `/users/{id}` | `{ name, deptId, status }` | ADMIN |
| GET | `/departments` | — | all authenticated |
| POST | `/departments` | `{ name, parentId }` | ADMIN |
| PUT | `/departments/{id}` | `{ name, parentId, headId }` | ADMIN |
| DELETE | `/departments/{id}` | — | ADMIN — blocked (409) if active users reference it |

## Asset Categories (`/asset-categories`)

| Method | Path | Body | Roles |
|---|---|---|---|
| GET | `/asset-categories` | — | all authenticated |
| POST | `/asset-categories` | `{ name, description, warrantyMonths }` | ADMIN, ASSET_MANAGER |
| PUT | `/asset-categories/{id}` | same | ADMIN, ASSET_MANAGER |
| DELETE | `/asset-categories/{id}` | — | ADMIN, ASSET_MANAGER — blocked (409) if in use by any asset |

## Assets (`/assets`)

| Method | Path | Body / Query | Roles |
|---|---|---|---|
| GET | `/assets` | query: `category`, `status`, `assignedTo`, `q` (search) | all authenticated (scoped: employees see only assigned/available) |
| GET | `/assets/{id}` | — | all authenticated |
| POST | `/assets` | see below | ADMIN, ASSET_MANAGER |
| PUT | `/assets/{id}` | partial update | ADMIN, ASSET_MANAGER — rejected (409) unless asset status is `AVAILABLE` |
| DELETE | `/assets/{id}` | — | ADMIN, ASSET_MANAGER — soft-delete, transitions to `RETIRED` |
| POST | `/assets/{id}/documents` | multipart file | ADMIN, ASSET_MANAGER |
| GET | `/assets/{id}/documents` | — | all authenticated with asset visibility |

`POST /assets` request:
```json
{
  "assetTag": "AF-001",
  "serialNo": "SN12345",
  "name": "Dell Laptop",
  "categoryId": 2,
  "location": "HQ",
  "purchaseDate": "2026-07-01",
  "cost": 1200.00,
  "condition": "New",
  "bookable": false
}
```
New assets always start at `status = AVAILABLE` — clients cannot set initial
status.

## Allocations, Transfers, Returns

| Method | Path | Body | Roles |
|---|---|---|---|
| GET | `/allocations` | query: `userId`, `assetId`, `status` | ADMIN, ASSET_MANAGER (all); DEPT_HEAD (dept); EMPLOYEE (self) |
| POST | `/allocations` | `{ assetId, userId, expectedReturn }` | ADMIN, ASSET_MANAGER |
| POST | `/transfers` | `{ assetId, newUserId }` | ADMIN, ASSET_MANAGER, DEPT_HEAD (dept), EMPLOYEE (self, request only) |
| PUT | `/transfers/{id}/approve` | — | ADMIN, ASSET_MANAGER, DEPT_HEAD (dept) |
| PUT | `/transfers/{id}/reject` | `{ reason }` | ADMIN, ASSET_MANAGER, DEPT_HEAD (dept) |
| POST | `/returns` | `{ allocationId, condition }` | current holder, or ADMIN/ASSET_MANAGER/DEPT_HEAD on their behalf |

`POST /allocations` errors on an unavailable asset:
```json
{
  "success": false,
  "message": "Asset AF-001 is already allocated to Alice Johnson. Consider a transfer request.",
  "data": null,
  "timestamp": "2026-07-12T10:15:30Z"
}
```
HTTP 409 Conflict.

## Resources & Bookings

| Method | Path | Body | Roles |
|---|---|---|---|
| GET | `/resources` | — | all authenticated |
| POST | `/resources` | `{ name, description, location }` | ADMIN, ASSET_MANAGER |
| GET | `/bookings` | query: `userId`, `resourceId`, `from`, `to` | all authenticated (scoped) |
| POST | `/bookings` | `{ resourceId, start, end }` | all authenticated |
| DELETE | `/bookings/{id}` | — | owner, or ADMIN/ASSET_MANAGER/DEPT_HEAD(dept) |

`POST /bookings` conflict error:
```json
{
  "success": false,
  "message": "Booking conflict: Conf Room A is already booked during this time.",
  "data": null,
  "timestamp": "2026-07-12T10:15:30Z"
}
```
HTTP 409 Conflict. Adjacent bookings (`existing.end == new.start`) are allowed —
see overlap predicate in `04-BUSINESS-RULES.md`.

## Maintenance

| Method | Path | Body | Roles |
|---|---|---|---|
| GET | `/maintenance` | query: `assetId`, `status` | scoped per RBAC matrix |
| POST | `/maintenance` | `{ assetId, description, priority }` | all authenticated |
| PUT | `/maintenance/{id}/approve` | — | ADMIN, ASSET_MANAGER, DEPT_HEAD (dept) |
| PUT | `/maintenance/{id}/assign` | `{ technicianId }` | ADMIN, ASSET_MANAGER |
| PUT | `/maintenance/{id}/status` | `{ status: "RESOLVED" }` | ADMIN, ASSET_MANAGER |

Every status transition writes a `maintenance_history` row; invalid transitions
(see state machine) return 400.

## Audits

| Method | Path | Body | Roles |
|---|---|---|---|
| POST | `/audits` | `{ title, startDate, endDate }` | ADMIN, ASSET_MANAGER |
| POST | `/audits/{id}/items` | `{ assetId, auditorId }` | ADMIN, ASSET_MANAGER |
| PUT | `/audits/{id}/items/{itemId}` | `{ result, remarks }` | assigned auditor only |
| PUT | `/audits/{id}/close` | — | ADMIN, ASSET_MANAGER — locks all items, read-only after |

## Dashboard, Reports, Notifications, Activity Log

| Method | Path | Notes |
|---|---|---|
| GET | `/dashboard/summary` | counts: available/allocated/under-maintenance assets, pending allocations, upcoming bookings; scoped by role |
| GET | `/dashboard/assets-by-department` | bar-chart data, GROUP BY dept |
| GET | `/dashboard/maintenance-frequency` | time-series aggregate |
| GET | `/reports/assets?format=csv` | ADMIN, ASSET_MANAGER |
| GET | `/reports/maintenance?format=pdf` | ADMIN, ASSET_MANAGER |
| GET | `/notifications` | current user's notifications, `?unreadOnly=true` |
| PUT | `/notifications/{id}/read` | mark one read |
| GET | `/activity-log` | ADMIN only, query: `userId`, `from`, `to` |

## Error status code conventions

| Situation | Status |
|---|---|
| Validation failure (Bean Validation) | 400 |
| Business rule violation (double allocation, invalid state transition) | 400 |
| Resource conflict (booking overlap, duplicate unique field) | 409 |
| Not authenticated / expired token | 401 |
| Authenticated but not authorized | 403 |
| Resource not found | 404 |
| Unexpected server error | 500 (generic message only, logged server-side) |
