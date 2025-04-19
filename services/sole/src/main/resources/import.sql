CREATE TABLE IF NOT EXISTS tb_registrations (
    id SERIAL PRIMARY KEY,
    value VARCHAR(80) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_roleplay (
    id SERIAL PRIMARY KEY,
    id_registration INT NOT NULL REFERENCES tb_registrations(id) ON DELETE CASCADE,
    selected_role VARCHAR(12) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);
