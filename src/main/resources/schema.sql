DROP TABLE IF EXISTS mpa, GENRES, FILMS, FILM_GENRES, USERS, LIKES, FRIENDS;

CREATE TABLE if not exists users (
  user_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar(20),
  login varchar(20),
  user_name varchar(20),
  birthday date
);

CREATE TABLE if not exists mpa (
  id integer PRIMARY KEY,
  mpa_name varchar
    --CHECK (duration BETWEEN 1 AND 200)
);

CREATE TABLE if not exists films (
  film_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(50),
  description varchar(200),
  release_date date,
  duration integer,
  mpa_id integer REFERENCES mpa (id)
  --CHECK (duration BETWEEN 1 AND 200)
);

CREATE TABLE if not exists genres (
  genre_id integer PRIMARY KEY,
  genre_name varchar(20)
);

CREATE TABLE if not exists film_genres (
  film_id long REFERENCES films (film_id),
  genre_id integer REFERENCES genres (genre_id)
  --PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE if not exists likes (
  film_id long REFERENCES films (film_id),
  user_id long REFERENCES users (user_id)
  --PRIMARY KEY (film_id, user_id)
);

CREATE TABLE if not exists friends (
  user_id long REFERENCES users (user_id),
  friend_id long REFERENCES users (user_id),
  status boolean
  --PRIMARY KEY (user_id, friend_id)
);

--ALTER TABLE if not exists films_genres ADD FOREIGN KEY (film_id) REFERENCES films (id);

--ALTER TABLE films_genres ADD FOREIGN KEY (genre_id) REFERENCES genres (genre_id) if not exists;

--ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES films  (id) if not exists;

--ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (user_id) if not exists;

--ALTER TABLE friends ADD FOREIGN KEY (user_id) REFERENCES users (user_id) if not exists;

--ALTER TABLE friends ADD FOREIGN KEY (friend_id) REFERENCES users (user_id) if not exists;