package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Optional;
public interface FilmStorage {

    long generateId();

    Optional<Film> saveFilm(Film film);

    boolean validateFilm(Film film);

    HashMap<Long, Film> getFilms();
}
