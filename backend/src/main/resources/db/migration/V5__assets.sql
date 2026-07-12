CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    asset_tag VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    manufacturer VARCHAR(100),
    model VARCHAR(100),
    purchase_price NUMERIC(10, 2),
    purchase_date DATE,
    warranty_expiry DATE,
    lifecycle_status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    category_id BIGINT NOT NULL REFERENCES asset_categories(id),
    department_id BIGINT REFERENCES departments(id),
    assigned_user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);
CREATE INDEX idx_assets_asset_tag ON assets(asset_tag);
CREATE INDEX idx_assets_serial_number ON assets(serial_number);
CREATE INDEX idx_assets_category_id ON assets(category_id);
CREATE INDEX idx_assets_assigned_user_id ON assets(assigned_user_id);
CREATE INDEX idx_assets_department_id ON assets(department_id);
CREATE INDEX idx_assets_status ON assets(status);
CREATE INDEX idx_assets_lifecycle_status ON assets(lifecycle_status);