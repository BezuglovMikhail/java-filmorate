package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;

@Repository
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HashSet<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(jdbcTemplate.query("SELECT genres.genre_id, genre_name FROM film_genres" +
                        " JOIN genres ON film_genres.genre_id = genres.genre_id" +
                        " WHERE film_id = ?" +
                        " ORDER BY genres.genre_id ", (rs, rowNum) -> new Genre(
                        rs.getInt("genres.genre_id"),
                        rs.getString("genres.genre_name")),
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

    @Override
    public Collection<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", ((rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"))
        ));
    }

    @Override
    public Genre getGenreById(int genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT genre_name FROM genres WHERE genre_id = ?", genreId);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreId,
                    genreRows.getString("genre_name")
            );
            log.info("Найденный жанр = {} ", genre);
            return genre;
        } else throw new NotFoundException(String.format("Жанр с id= " + genreId + " не найден"));
    }
}
