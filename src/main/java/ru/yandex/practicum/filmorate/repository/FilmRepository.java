package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

@Data
public class FilmRepository {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int idFilm = 0;

    public int generateId() {
        return ++idFilm;
    }

    public void saveFilm(Film film) {
        //if (validateFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            } else {
                film.setId(generateId());
                films.put(film.getId(), film);
            //}
        }
    }

    public boolean validateFilm (Film film) {
        if (film.getReleaseDate().isAfter(LocalDate.parse("28.12.1895"))) {
            throw new ValidationException("Дата релиза не может быть позже :" + LocalDate.parse("28.12.1895"));
                    }
        return true;
    }
}