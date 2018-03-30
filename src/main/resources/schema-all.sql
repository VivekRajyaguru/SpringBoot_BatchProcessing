DROP TABLE user IF EXISTS;

CREATE TABLE user  (
    user_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);