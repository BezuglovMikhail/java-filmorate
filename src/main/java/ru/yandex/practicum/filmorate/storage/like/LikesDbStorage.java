package ru.yandex.practicum.filmorate.storage.like;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Repository
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final Logger log = LoggerFactory.getLogger(LikesDbStorage.class);

    public LikesDbStorage(JdbcTemplate jdbcTemplate,
                          GenreStorage genreStorage,
                          MpaStorage mpaStorage,
                          @Qualifier("filmDbStorage") FilmStorage filmStorage,
                          @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        if (!userStorage.validateUser(userId)) {
            throw new UserNotFoundException("Пользователя с id= " + userId + " не найден");
        }
        if (!filmStorage.validateFilm(filmId)) {
            throw new FilmNotFoundException("Фильма с id= " + filmId + " не найден");
        }
        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) VALUES (?, ?)", userId, filmId);
        log.info("Пользователь с id={} поставил лайк фильму с id={}", userId, filmId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (!userStorage.validateUser(userId)) {
            throw new UserNotFoundException("Пользователя с id= " + userId + " не найден");
        }
        if (!filmStorage.validateFilm(filmId)) {
            throw new FilmNotFoundException("Фильма с id= " + filmId + " не найден");
        }
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?", userId, filmId);
        log.info("Пользователь с id={} удалил лайк фильму с id={}", userId, filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return jdbcTemplate.query("SELECT films.film_id, films.name, films.description, films.release_date, films.duration, films.mpa_id, " +
                "COUNT(l.user_id) AS rating FROM films LEFT JOIN likes AS l ON films.film_id = l.film_id " +
                "GROUP BY films.film_id " +
                "ORDER BY rating DESC LIMIT ? ", (rs, rowNum) -> new Film(
                rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                genreStorage.getFilmGenres(rs.getLong("film_id")),
                mpaStorage.getMpa(rs.getInt("mpa_id")),
                rs.getLong("rating")
        ), count);
    }
}
