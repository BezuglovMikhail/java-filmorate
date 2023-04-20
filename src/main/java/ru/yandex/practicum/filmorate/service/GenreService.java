package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
@Slf4j
public class GenreService {
    private final JdbcTemplate jdbcTemplate;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Genre> get() {
        return jdbcTemplate.query("SELECT * FROM genres", ((rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"))
        ));
    }

    public Genre get(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT genre_name FROM genres WHERE genre_id", id);
        if (userRows.next()) {
            Genre genre = new Genre(
                    id,
                    userRows.getString("genre_name")
            );
            log.info("Найденный жанр = {} ", genre);
            return genre;
        } else throw new NotFoundException(String.format("Жанр с id=%d не найден", id));
    }


}

