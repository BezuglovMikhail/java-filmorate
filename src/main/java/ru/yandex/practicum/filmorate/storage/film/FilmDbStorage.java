package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long generateId() {
        return 0;
    }

    @Override
    public Optional<Film> saveFilm(Film film) {
        String sqlQuery = "insert into films(film_name, description, realise_date, duration, rating) " +
                            "values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingId());

        return Optional.empty();
    }

    @Override
    public boolean validateFilm(Film film) {
        return false;
    }

    @Override
    public HashMap<Long, Film> getFilms() {
        return null;
    }
}
