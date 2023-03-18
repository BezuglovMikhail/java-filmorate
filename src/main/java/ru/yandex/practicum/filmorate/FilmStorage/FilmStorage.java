package ru.yandex.practicum.filmorate.FilmStorage;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

@Data
public class FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int idFilm = 0;

    public int generateId() {
        return ++idFilm;
    }

    public void saveFilm(Film film) {
        if (validateFilm (film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            } else {
                film.setId(generateId());
                films.put(film.getId(), film);
            }
        }
    }

   public boolean validateFilm (Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше: 28.12.1895");
        }
        if (!getFilms().containsKey(film.getId()) && film.getId() != 0) {
            throw new ValidationException("Фильма с id = " + film.getId() + " нет.");
        }
        return true;
    }
}