-- Drop tables if they exist to start fresh
DROP TABLE IF EXISTS refresh_tokens CASCADE;
DROP TABLE IF EXISTS activity_logs CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS audit_items CASCADE;
DROP TABLE IF EXISTS audit_cycles CASCADE;
DROP TABLE IF EXISTS maintenance_history CASCADE;
DROP TABLE IF EXISTS maintenance_requests CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS resources CASCADE;
DROP TABLE IF EXISTS returns CASCADE;
DROP TABLE IF EXISTS transfers CASCADE;
DROP TABLE IF EXISTS allocations CASCADE;
DROP TABLE IF EXISTS asset_documents CASCADE;
DROP TABLE IF EXISTS assets CASCADE;
DROP TABLE IF EXISTS asset_categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- 1. roles
CREATE TABLE roles (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,   -- ADMIN, ASSET_MANAGER, DEPT_HEAD, EMPLOYEE
    description VARCHAR(255)
);

-- 2. departments (temporary create without head_id constraint to avoid circular dependency)
CREATE TABLE departments (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(120) NOT NULL,
    parent_id  BIGINT REFERENCES departments(id),
    head_id    BIGINT,
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_departments_parent_id ON departments(parent_id);

-- 3. users
CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(120) NOT NULL,
    email         VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id       BIGINT NOT NULL REFERENCES roles(id),
    dept_id       BIGINT REFERENCES departments(id),
    status        VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_dept_id ON users(dept_id);

-- Add head_id constraint back to departments now that users table exists
ALTER TABLE departments ADD CONSTRAINT fk_departments_head_id FOREIGN KEY (head_id) REFERENCES users(id);

-- 4. asset_categories
CREATE TABLE asset_categories (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(120) NOT NULL UNIQUE,
    description      VARCHAR(500),
    warranty_months  INTEGER,
    status           VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by       BIGINT REFERENCES users(id),
    updated_by       BIGINT REFERENCES users(id)
);

-- 5. assets
CREATE TABLE assets (
    id             BIGSERIAL PRIMARY KEY,
    asset_tag      VARCHAR(50) NOT NULL UNIQUE,
    serial_no      VARCHAR(100) UNIQUE,
    name           VARCHAR(160) NOT NULL,
    category_id    BIGINT NOT NULL REFERENCES asset_categories(id),
    location       VARCHAR(160),
    purchase_date  DATE,
    cost           NUMERIC(12,2),
    condition      VARCHAR(30),
    status         VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    bookable       BOOLEAN NOT NULL DEFAULT false,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by     BIGINT REFERENCES users(id),
    updated_by     BIGINT REFERENCES users(id)
);
CREATE INDEX idx_assets_category_id ON assets(category_id);
CREATE INDEX idx_assets_status ON assets(status);

-- 6. asset_documents
CREATE TABLE asset_documents (
    id          BIGSERIAL PRIMARY KEY,
    asset_id    BIGINT NOT NULL REFERENCES assets(id),
    file_name   VARCHAR(255) NOT NULL,
    file_url    VARCHAR(500) NOT NULL,
    uploaded_by BIGINT REFERENCES users(id),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_asset_documents_asset_id ON asset_documents(asset_id);

-- 7. allocations
CREATE TABLE allocations (
    id              BIGSERIAL PRIMARY KEY,
    asset_id        BIGINT NOT NULL REFERENCES assets(id),
    user_id         BIGINT NOT NULL REFERENCES users(id),
    allocated_by    BIGINT NOT NULL REFERENCES users(id),
    start_date      DATE NOT NULL,
    expected_return DATE,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE / RETURNED
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_allocations_asset_id ON allocations(asset_id);
CREATE INDEX idx_allocations_user_id ON allocations(user_id);
CREATE UNIQUE INDEX uq_allocations_one_active_per_asset
    ON allocations(asset_id) WHERE status = 'ACTIVE';

-- 8. transfers
CREATE TABLE transfers (
    id             BIGSERIAL PRIMARY KEY,
    asset_id       BIGINT NOT NULL REFERENCES assets(id),
    from_user_id   BIGINT NOT NULL REFERENCES users(id),
    to_user_id     BIGINT NOT NULL REFERENCES users(id),
    requested_by   BIGINT NOT NULL REFERENCES users(id),
    request_date   TIMESTAMPTZ NOT NULL DEFAULT now(),
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING / APPROVED / REJECTED
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_transfers_asset_id ON transfers(asset_id);

-- 9. returns
CREATE TABLE returns (
    id             BIGSERIAL PRIMARY KEY,
    allocation_id  BIGINT NOT NULL REFERENCES allocations(id),
    returned_by    BIGINT NOT NULL REFERENCES users(id),
    return_date    DATE NOT NULL,
    condition      VARCHAR(30),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_returns_allocation_id ON returns(allocation_id);

-- 10. resources
CREATE TABLE resources (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(500),
    location    VARCHAR(160),
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 11. bookings
CREATE TABLE bookings (
    id          BIGSERIAL PRIMARY KEY,
    resource_id BIGINT NOT NULL REFERENCES resources(id),
    user_id     BIGINT NOT NULL REFERENCES users(id),
    start_time  TIMESTAMPTZ NOT NULL,
    end_time    TIMESTAMPTZ NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED', -- PENDING / CONFIRMED / CANCELLED / COMPLETED
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_booking_time_order CHECK (end_time > start_time)
);
CREATE INDEX idx_bookings_resource_time ON bookings(resource_id, start_time, end_time);

-- 12. maintenance_requests
CREATE TABLE maintenance_requests (
    id           BIGSERIAL PRIMARY KEY,
    asset_id     BIGINT NOT NULL REFERENCES assets(id),
    requested_by BIGINT NOT NULL REFERENCES users(id),
    description  VARCHAR(1000),
    priority     VARCHAR(20) NOT NULL DEFAULT 'MEDIUM', -- LOW / MEDIUM / HIGH
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING',
        -- PENDING / APPROVED / IN_PROGRESS / RESOLVED / CLOSED
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_maintenance_requests_asset_id ON maintenance_requests(asset_id);
CREATE INDEX idx_maintenance_requests_status ON maintenance_requests(status);

-- 13. maintenance_history
CREATE TABLE maintenance_history (
    id          BIGSERIAL PRIMARY KEY,
    request_id  BIGINT NOT NULL REFERENCES maintenance_requests(id),
    status      VARCHAR(20) NOT NULL,
    updated_by  BIGINT NOT NULL REFERENCES users(id),
    comment     VARCHAR(500),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_maintenance_history_request_id ON maintenance_history(request_id);

-- 14. audit_cycles
CREATE TABLE audit_cycles (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(160) NOT NULL,
    start_date DATE NOT NULL,
    end_date   DATE,
    created_by BIGINT NOT NULL REFERENCES users(id),
    status     VARCHAR(20) NOT NULL DEFAULT 'OPEN', -- OPEN / CLOSED
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 15. audit_items
CREATE TABLE audit_items (
    id          BIGSERIAL PRIMARY KEY,
    audit_id    BIGINT NOT NULL REFERENCES audit_cycles(id),
    asset_id    BIGINT NOT NULL REFERENCES assets(id),
    auditor_id  BIGINT NOT NULL REFERENCES users(id),
    result      VARCHAR(20), -- PENDING / VERIFIED / MISSING / DAMAGED
    remarks     VARCHAR(500),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_audit_items_audit_id ON audit_items(audit_id);
CREATE INDEX idx_audit_items_asset_id ON audit_items(asset_id);

-- 16. notifications
CREATE TABLE notifications (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id),
    type       VARCHAR(50) NOT NULL,
    message    VARCHAR(500) NOT NULL,
    is_read    BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_notifications_user_unread ON notifications(user_id, is_read);

-- 17. activity_logs
CREATE TABLE activity_logs (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users(id),
    module     VARCHAR(50) NOT NULL,
    action     VARCHAR(80) NOT NULL,
    details    VARCHAR(1000),
    timestamp  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_activity_logs_user_created ON activity_logs(user_id, timestamp);

-- 18. refresh_tokens
CREATE TABLE refresh_tokens (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id),
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked    BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
