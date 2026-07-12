CREATE TABLE bookable_resources (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    resource_type VARCHAR(50),
    description TEXT,
    category_id BIGINT REFERENCES asset_categories(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);