package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;

@Repository
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public HashSet<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(jdbcTemplate.query(
                "SELECT genres.genre_id, genres.genre_name FROM film_genres\n" +
                        " JOIN genres ON film_genres.genre_id = genres.genre_id\n" +
                        " WHERE film_id = ?\n" +
                        " ORDER BY genres.genre_id", (rs, rowNum) -> new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre_name")),
                filmId
        ));
    }

    @Override
    public void addGenres(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(
                        "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", film.getId(), genre.getId()
                );
            }
        }
    }

    @Override
    public void removeGenres(Film film) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
    }
}
