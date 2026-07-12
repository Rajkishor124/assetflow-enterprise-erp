# AssetFlow ERP — Project Status Tracker

This document serves as the single source of truth for the development progress of the AssetFlow Enterprise ERP system. It tracks the status of both backend and frontend components, database migrations, known issues, and next milestones.

---

## 1. Current Sprint

**Sprint Goal:** Complete Frontend Feature Integration for Assets and Organizations.

## Current Implementation Phase
**Phase 3: Frontend Feature Integration**
- [ ] List View for Assets with server-side pagination & filtering
- [ ] Asset Creation & Editing Forms
- [ ] Department & User Management Views

### Completed Milestones
- **Milestone 1**: Core Data Model & Migrations
- **Milestone 2**: Backend REST API Controllers (Asset & Organization)
- **Milestone 3**: Backend-Frontend API Contract Synchronization (Merged branches to `main`)

- **In Progress:**
  - Designing frontend React views for Assets and Organization
- **Blocked:**
  - None.
- **Next:**
  - TanStack Query integrations for Departments, Users, and Assets.

---

## 2. Project Overview

AssetFlow is a modern Enterprise Asset and Resource Management ERP designed to:
- Manage the complete lifecycle of corporate assets (Available, Allocated, Under Maintenance, Lost, Retired).
- Handle shared resource bookings (rooms, vehicles, equipment) with conflict-free overlap checking.
- Streamline maintenance requests and history tracking.
- Orchestrate scheduled inventory audits and flag discrepancies.
- Generate dashboard metrics and reports.

---

## 3. Current Branch Information

The project uses `main` as the primary development branch.
- **`main`** (Active): Contains the fully merged and synchronized code for both backend APIs and frontend setup.
- **`backend`**: Deleted.
- **`frontend`**: Deleted.

---

## 4. Feature Progress Matrix

| Module | Backend (Entity/Repo) | Backend (Service) | Backend (API/Controller) | Frontend (UI) | Integration | Status |
| :--- | :---: | :---: | :---: | :---: | :---: | :--- |
| **Authentication** | ✅ | ✅ | ✅ | ✅ | ✅ | **Complete** |
| **Organization** | ✅ | ✅ (Refactored) | ✅ | ❌ | ❌ | **API Complete** |
| **Asset Categories** | ✅ | ✅ | ✅ | ❌ | ❌ | **API Complete** |
| **Asset Management** | ✅ | ✅ | ✅ | ❌ | ❌ | **API Complete** |
| **Asset Documents** | ✅ | ✅ | ❌ | ❌ | ❌ | **Services Complete** |
| **Allocations & Returns** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |
| **Resource Bookings** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |
| **Asset Maintenance** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |
| **Inventory Audits** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |
| **Dashboard Summary** | ✅ | ❌ | ❌ | 🟡 (Layout Only) | ❌ | **In Progress** |
| **CSV/PDF Reports** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |
| **Notifications** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |
| **Activity Logging** | ✅ | ❌ | ❌ | ❌ | ❌ | **Database Only** |

*Legend: ✅ Complete | 🟡 Partial / Placeholder | ❌ Not Started*

---

## 5. Technical Status

### 5.1. Backend Status (`backend` branch)
- **Technology Stack**: Spring Boot 3.4.0, Java 21, Spring Security 6, Spring Data JPA, MapStruct, Lombok, Flyway.
- **Build & Tests**: Compiles successfully. Standard tests (`AuthFlowIntegrationTest`, `DepartmentCommandServiceTest`, `AssetCommandServiceTest`) pass.
- **Service Architecture**:
  - Implements Command & Query separation (CQRS-lite).
  - Uses `BaseCommandService` and `BaseQueryService` to centralize entity checks and pagination.
  - Implements custom event pub-sub (e.g. `AssetCreatedEvent`) with transactional listeners.
- **Missing Elements**: REST Controllers are missing for all modules except `/api/v1/auth`.

### 5.2. Frontend Status (`frontend` branch)
- **Technology Stack**: Next.js 16.2.10 (App Router), TypeScript, TailwindCSS, Lucide Icons, Axios.
- **Build & Tests**: Compiles and builds successfully with `0` TypeScript or linting errors.
- **Implemented Views**:
  - Landing Page (`/`): Implemented with modern styling and platform entry points.
  - Login (`/login`): Fully functional form, integrates with `/auth/login` API, decodes JWT token on client side, and stores user in `localStorage`.
  - Signup (`/signup`): Functional form, integrates with `/auth/signup` API.
  - Dashboard (`/dashboard`): Modern layout shell with sidebar navigation, metrics widgets, and placeholder components.
- **Resolved Issues Today**: Resolved compile-blocking missing `../lib/auth` imports and type casting strictness issues on client-side login handlers.
- **Missing Elements**: Actual pages for assets, bookings, departments, users, and audits do not exist yet.

### 5.3. Database Status
- **PostgreSQL Database**: Currently seeded with 19 migrations (`V1` to `V19`).
- **Flyway Status**: Schema history table is synchronized with `V19__seed_roles.sql` (checksum `-1642306183`).
- **H2 Test Database**: Configured to disable Flyway in `application-test.yml` and use Hibernate `create-drop` + `@Sql("/seed_roles.sql")` to prevent PostgreSQL syntax errors (`ON CONFLICT`) during local testing.

---

## 6. Known Issues & Technical Debt

1. **Missing Frontend Pages**: Pages for managing assets, departments, users, and bookings do not exist.

---

## 7. Next Milestones

### Milestone 1: REST API Exposure (Controllers)
- **Objective**: Create thin REST Controllers for `Organization` (Department, User) and `Assets` (Asset, AssetCategory, AssetDocument) to expose already-implemented service layers.
- **Endpoint List**:
  - `/api/v1/departments` (CRUD)
  - `/api/v1/users` (Read, Assign Dept, Change Role)
  - `/api/v1/asset-categories` (CRUD)
  - `/api/v1/assets` (CRUD, upload documents)

### Milestone 2: Frontend Integration for Org & Assets
- **Objective**: Build pages and TanStack Query integrations in the frontend to manage departments, users, and assets.
- **Pages**:
  - `/admin/departments`
  - `/admin/users`
  - `/assets`

### Milestone 3: Allocations & Return Workflows
- **Objective**: Implement the backend business service layer, controllers, and frontend pages for asset allocations, transfer requests, and returns.

---
*Last Updated: 2026-07-12 15:30 (UTC+5:30)*
