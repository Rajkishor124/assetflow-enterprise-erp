CREATE TABLE maintenance_requests (
    id BIGSERIAL PRIMARY KEY,
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    reported_by_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority VARCHAR(30) NOT NULL DEFAULT 'MEDIUM',
    maintenance_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    scheduled_date TIMESTAMP WITH TIME ZONE,
    resolution_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);
CREATE INDEX idx_maintenance_requests_asset_id ON maintenance_requests(asset_id);
CREATE INDEX idx_maintenance_requests_status ON maintenance_requests(maintenance_status);