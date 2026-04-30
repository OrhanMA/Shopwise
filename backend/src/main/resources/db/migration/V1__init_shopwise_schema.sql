CREATE TABLE shops (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160),
    phone VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(160) NOT NULL,
    phone VARCHAR(30),
    loyalty_points INTEGER NOT NULL DEFAULT 0 CHECK (loyalty_points >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customers_shop_email UNIQUE (shop_id, email)
);

CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    name VARCHAR(120) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
    points_reward INTEGER NOT NULL DEFAULT 100 CHECK (points_reward >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_services_shop_name UNIQUE (shop_id, name)
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    service_id BIGINT NOT NULL REFERENCES services(id),
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    notes TEXT,
    honored_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_appointments_status CHECK (status IN ('SCHEDULED', 'HONORED', 'CANCELLED')),
    CONSTRAINT uq_appointments_slot UNIQUE (shop_id, appointment_date, appointment_time, service_id)
);

CREATE TABLE loyalty_transactions (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    appointment_id BIGINT REFERENCES appointments(id),
    points INTEGER NOT NULL CHECK (points <> 0),
    reason VARCHAR(160) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_loyalty_transaction_type CHECK (transaction_type IN ('EARNED', 'ADJUSTED', 'CANCELLED'))
);

CREATE INDEX idx_customers_shop_last_name ON customers(shop_id, last_name);
CREATE INDEX idx_appointments_shop_date ON appointments(shop_id, appointment_date);
CREATE INDEX idx_appointments_shop_status ON appointments(shop_id, status);
CREATE INDEX idx_appointments_customer ON appointments(customer_id);
CREATE INDEX idx_loyalty_transactions_customer ON loyalty_transactions(customer_id, created_at);
