CREATE TABLE resource_bookings (
    id BIGSERIAL PRIMARY KEY,
    resource_id BIGINT NOT NULL REFERENCES bookable_resources(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    booking_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    purpose TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0
);
CREATE INDEX idx_resource_bookings_dates ON resource_bookings(start_time, end_time);
CREATE INDEX idx_resource_bookings_resource_id ON resource_bookings(resource_id);