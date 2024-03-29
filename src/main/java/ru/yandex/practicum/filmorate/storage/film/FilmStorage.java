package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    long generateId();

    Optional<Film> saveFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Film removeFilm(Film film);

    boolean validateFilm(Long filmId);

    Collection<Film> getFilms();

    Film findFilmById(Long filmId);

    void addLike(Long userId, Long filmId);

    void removeLike(Long filmId, Long userId);

    Collection<Film> getPopular(int count);
}
