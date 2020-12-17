CREATE TABLE IF NOT EXISTS fun_data (
    id SERIAL PRIMARY KEY,
    content varchar
);
CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    created timestamp NOT NULL,
    user1 varchar NOT NULL,
    user2 varchar,
    first_to_move varchar,
    CHECK (first_to_move == user1 or first_to_move == user2),
    CHECK (user1 != user2),
    moves integer[9]
);
