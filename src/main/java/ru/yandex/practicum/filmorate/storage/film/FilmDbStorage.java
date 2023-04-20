package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, GenreDbStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaDbStorage;
        this.genreStorage = genreStorage;
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
                    "UPDATE films SET name = ?, description = ?, release_date = ?," +
                            " duration = ?, mpa_id = ? WHERE film_id = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
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
            throw new NotFoundException(String.format("Фильма с id = " + film.getId() + " нет."));
        }
    }

    @Override
    public boolean validateFilm(Long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", filmId);
        return filmRows.next();
    }

    @Override
    public Collection<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM films", (rs, rowNum) -> new Film(
                rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                genreStorage.getFilmGenres(rs.getLong("film_id")),
                mpaStorage.getMpa(rs.getInt("mpa_id"))
        ));
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", filmId);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );
            int mpaId = filmRows.getInt("mpa_id");
            film.setMpa(mpaStorage.getMpa(mpaId));
            Set<Genre> genres = genreStorage.getFilmGenres(filmId);
            film.setGenres(genreStorage.getFilmGenres(filmId));
            log.info("Найден фильм: {} {}", filmRows.getString("film_id"), filmRows.getString("name"));
            return Optional.of(film);
        } else {
            throw new NotFoundException(String.format("Фильма с id = " + filmId + " нет."));
        }
    }
}

