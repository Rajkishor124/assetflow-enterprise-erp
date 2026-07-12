DELETE FROM users;
DELETE FROM roles;

INSERT INTO roles (name, description, created_at, updated_at, status, version)
VALUES
    ('ADMIN', 'Full system control', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', 0),
    ('ASSET_MANAGER', 'Owns asset lifecycle', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', 0),
    ('DEPT_HEAD', 'Manages department', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', 0),
    ('EMPLOYEE', 'Standard employee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', 0);
