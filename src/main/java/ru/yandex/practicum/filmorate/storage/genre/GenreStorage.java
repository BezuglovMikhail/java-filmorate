package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;

public interface GenreStorage {

    HashSet<Genre> getFilmGenres(Long id);

    void addGenres(Film film);

    void removeGenres(Film film);
}
