package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenreDbStorage genreStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.userStorage = userStorage;
    }

    @Override
    public long generateId() {
        return 0;
    }

    @Override
    public Optional<Film> saveFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        genreStorage.addGenres(film);
        jdbcTemplate.update("UPDATE films SET mpa_id = ? WHERE film_id = ?", film.getMpa().getId(), film.getId());
        log.info("Фильм с идентификатором {} добавлен.", film.getId());
        return Optional.ofNullable(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (validateFilm(film.getId())) {
            jdbcTemplate.update(
                    "UPDATE films SET name = ?, " +
                            "description = ?, " +
                            "release_date = ?, " +
                            "duration = ?, " +
                            "mpa_id = ? " +
                            "WHERE film_id = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            genreStorage.removeGenres(film);
            genreStorage.addGenres(film);
            log.info("Данные фильма с идентификатором {} обновлены.", film.getId());
            return Optional.of(film);
        } else {
            throw new NotFoundException("Фильма с id = " + film.getId() + " нет.");
        }
    }

    @Override
    public Film removeFilm(Film film) {
        if (validateFilm(film.getId())) {
            jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", film.getId());
            log.info("Удален фильм с: id={}", film.getId());
            return film;

        } else {
            throw new NotFoundException("Фильма с id = " + film.getId() + " нет.");
        }
    }

    @Override
    public boolean validateFilm(Long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", filmId);
        return filmRows.next();
    }

    @Override
    public Collection<Film> getFilms() {
        return jdbcTemplate.query(
                "SELECT f.film_id, " +
                        "f.name, f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "m.id, " +
                        "m.mpa_name " +
                        "FROM films f JOIN mpa m ON f.mpa_id = m.id ",
                (rs, rowNum) -> new Film(
                        rs.getLong("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        genreStorage.getFilmGenres(rs.getLong("film_id")),
                        new MPA(rs.getInt("id"), rs.getString("mpa_name"))
                ));
    }

    @Override
    public Film findFilmById(Long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.film_id, " +
                        "f.name, " +
                        "f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "m.id, " +
                        "m.mpa_name " +
                        "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.id " +
                        "WHERE film_id = ?", filmId);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );

            film.setMpa(new MPA(filmRows.getInt("id"), filmRows.getString("mpa_name")));
            Set<Genre> genres = genreStorage.getFilmGenres(filmId);
            if (genres.size() != 0) {
                film.setGenres(genres);
            }
            log.info("Найден фильм: {} {}", filmRows.getString("film_id"), filmRows.getString("name"));
            return film;
        } else {
            throw new NotFoundException("Фильма с id = " + filmId + " нет.");
        }
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        if (!userStorage.validateUser(userId)) {
            throw new NotFoundException("Пользователя с id= " + userId + " не найден");
        }
        if (!validateFilm(filmId)) {
            throw new NotFoundException("Фильма с id= " + filmId + " не найден");
        }
        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) VALUES (?, ?)", userId, filmId);
        log.info("Пользователь с id={} поставил лайк фильму с id={}", userId, filmId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (!userStorage.validateUser(userId)) {
            throw new NotFoundException("Пользователя с id= " + userId + " не найден");
        }
        if (!validateFilm(filmId)) {
            throw new NotFoundException("Фильма с id= " + filmId + " не найден");
        }
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?", userId, filmId);
        log.info("Пользователь с id={} удалил лайк фильму с id={}", userId, filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return jdbcTemplate.query(
                "SELECT f.film_id, " +
                        "f.name, " +
                        "f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "f.mpa_id, " +
                        "COUNT(l.user_id) AS rating, " +
                        "m.mpa_name " +
                        "FROM films f LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                        "JOIN mpa AS m ON f.mpa_id = m.id " +
                        "GROUP BY f.film_id " +
                        "ORDER BY rating DESC LIMIT ? ", (rs, rowNum) -> new Film(
                        rs.getLong("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        genreStorage.getFilmGenres(rs.getLong("film_id")),
                        new MPA(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                        rs.getLong("rating")
                ), count);
    }
}
