CREATE TABLE audit_items (
    id BIGSERIAL PRIMARY KEY,
    audit_cycle_id BIGINT NOT NULL REFERENCES audit_cycles(id),
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    result VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    audited_by_id BIGINT REFERENCES users(id),
    audited_at TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);
CREATE INDEX idx_audit_items_cycle_id ON audit_items(audit_cycle_id);
CREATE INDEX idx_audit_items_asset_id ON audit_items(asset_id);