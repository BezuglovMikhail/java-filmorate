package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

public interface FilmStorage {

    long generateId();

    void saveFilm(Film film);

   boolean validateFilm (Film film);
   HashMap<Long, Film> getFilms();
   Long getFilmId();
}