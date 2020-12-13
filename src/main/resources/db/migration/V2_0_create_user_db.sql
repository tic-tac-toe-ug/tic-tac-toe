CREATE TABLE IF NOT EXISTS user_accounts (
    id SERIAL PRIMARY KEY,
    login varchar NOT NULL,
    email varchar NOT NULL,
    password varchar NOT NULL,
);
