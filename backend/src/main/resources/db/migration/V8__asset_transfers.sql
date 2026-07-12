CREATE TABLE asset_transfers (
    id BIGSERIAL PRIMARY KEY,
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    from_user_id BIGINT REFERENCES users(id),
    to_user_id BIGINT REFERENCES users(id),
    from_department_id BIGINT REFERENCES departments(id),
    to_department_id BIGINT REFERENCES departments(id),
    transfer_date TIMESTAMP WITH TIME ZONE NOT NULL,
    transfer_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);
CREATE INDEX idx_asset_transfers_asset_id ON asset_transfers(asset_id);