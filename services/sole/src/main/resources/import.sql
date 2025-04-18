CREATE TABLE IF NOT EXISTS collection_of_registrations (
    registration_id SERIAL PRIMARY KEY,
    signature VARCHAR(80) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS collection_of_role_assignments (
    role_assignment_id SERIAL PRIMARY KEY,
    registration_id INT NOT NULL REFERENCES collection_of_registrations(registration_id) ON DELETE CASCADE,
    selected_role VARCHAR(12) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);
