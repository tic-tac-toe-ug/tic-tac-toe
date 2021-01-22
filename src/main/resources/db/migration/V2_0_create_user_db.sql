CREATE TABLE IF NOT EXISTS user_accounts (
    id SERIAL PRIMARY KEY,
    username varchar NOT NULL,
    email varchar NOT NULL,
    password varchar NOT NULL,
);
