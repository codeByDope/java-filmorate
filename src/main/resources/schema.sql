DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS mpa_ratings CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS likers CASCADE;
DROP TABLE IF EXISTS films_genres CASCADE;

CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
email varchar NOT NULL UNIQUE,
login varchar NOT NULL UNIQUE,
name varchar NOT NULL,
birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
title varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
title varchar NOT NULL,
description text,
release_date date,
duration int NOT NULL,
rating_id INTEGER REFERENCES mpa_ratings(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friends (
main_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
friend_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likers (
film_id BIGINT NOT NULL REFERENCES films(id) ON DELETE CASCADE,
liker_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
title varchar
);

CREATE TABLE IF NOT EXISTS films_genres (
film_id BIGINT NOT NULL REFERENCES films(id) ON DELETE CASCADE,
genre_id int NOT NULL REFERENCES genres(id) ON DELETE CASCADE
);