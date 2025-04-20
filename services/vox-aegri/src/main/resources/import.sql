CREATE TYPE request_level AS ENUM ('LOW', 'LOW_TO_MEDIUM', 'MEDIUM', 'MEDIUM_TO_HIGH', 'HIGH');

CREATE TYPE request_status AS ENUM ('UNAVAILABLE', 'AVAILABLE', 'REJECTED', 'FULFILLED', 'ACCEPTED');

CREATE TABLE IF NOT EXISTS tb_customer_request (
    id SERIAL PRIMARY KEY,
    title VARCHAR(32) UNIQUE NOT NULL,
    description VARCHAR(240) NOT NULL,
    level request_level NOT NULL,
    base_reward FLOAT4 NOT NULL,
    is_active BOOLEAN NOT NULL,
    author VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_request_status (
    id SERIAL PRIMARY KEY,
    id_registration INT NOT NULL,
    id_request INT NOT NULL REFERENCES tb_customer_request(id) ON DELETE CASCADE,
    current_status request_status NOT NULL
);

CREATE INDEX idx_request_registration
    ON tb_request_status (id_request, id_registration);
