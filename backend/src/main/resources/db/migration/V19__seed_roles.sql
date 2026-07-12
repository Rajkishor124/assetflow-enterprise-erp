-- Seed the four application roles as defined in the RBAC matrix.
-- ON CONFLICT ensures idempotency if migration is re-applied.
INSERT INTO roles (name, description, created_at, updated_at, status, version)
VALUES
    ('ADMIN', 'Full system control: users, departments, roles, all data', NOW(), NOW(), 'ACTIVE', 0),
    ('ASSET_MANAGER', 'Owns asset lifecycle, allocations, bookings, maintenance, audits', NOW(), NOW(), 'ACTIVE', 0),
    ('DEPT_HEAD', 'Manages their own department and approves department requests', NOW(), NOW(), 'ACTIVE', 0),
    ('EMPLOYEE', 'Standard employee: requests things for themselves, views own data', NOW(), NOW(), 'ACTIVE', 0);
