package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private HashMap<Long, Film> films = new HashMap<>();
    private long idFilm = 0;

    @Override
    public long generateId() {
        return ++idFilm;
    }

    @Override
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

    @Override
    public boolean validateFilm (Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше: 28.12.1895");
        }
        if (!getFilms().containsKey(film.getId()) && film.getId() != 0) {
            throw new FilmNotFoundException("Фильма с id = " + film.getId() + " нет.");
        }
        return true;
    }

    @Override
    public HashMap<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Long getFilmId() {
        return idFilm;
    }
}
