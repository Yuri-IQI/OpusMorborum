CREATE TABLE IF NOT EXISTS tb_registration (
    id SERIAL PRIMARY KEY,
    value VARCHAR(80) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_roleplay (
    id SERIAL PRIMARY KEY,
    registration_id INT NOT NULL REFERENCES tb_registration(id) ON DELETE CASCADE,
    selected_role VARCHAR(12) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);
