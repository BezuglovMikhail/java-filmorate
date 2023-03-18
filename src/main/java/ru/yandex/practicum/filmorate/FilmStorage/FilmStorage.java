package ru.yandex.practicum.filmorate.FilmStorage;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

public interface FilmStorage {

    public int generateId();

    public void saveFilm(Film film);

   public boolean validateFilm (Film film);
}