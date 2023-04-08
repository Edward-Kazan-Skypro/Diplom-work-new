-- liquibase formatted sql

-- changeset team_unit:1

CREATE TABLE IF NOT EXISTS ads
(
    id          SERIAL PRIMARY KEY,
    author_id   integer REFERENCES users (id),
    price       int  NOT NULL,
    title       varchar,
    description varchar
);
