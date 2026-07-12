CREATE TABLE asset_allocations (
    id BIGSERIAL PRIMARY KEY,
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    user_id BIGINT REFERENCES users(id),
    department_id BIGINT REFERENCES departments(id),
    allocation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    expected_return_date TIMESTAMP WITH TIME ZONE,
    actual_return_date TIMESTAMP WITH TIME ZONE,
    allocation_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);
CREATE INDEX idx_asset_allocations_asset_id ON asset_allocations(asset_id);
CREATE INDEX idx_asset_allocations_user_id ON asset_allocations(user_id);
CREATE INDEX idx_asset_allocations_status ON asset_allocations(allocation_status);