package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long idFilm = 0;

    @Override
    public long generateId() {
        return ++idFilm;
    }

    @Override
    public Optional<Film> saveFilm(Film film) {
        if (validateFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            } else {
                film.setId(generateId());
                films.put(film.getId(), film);
            }
        }
        return films.values().stream()
                .filter(x -> Objects.equals(x.getName(), film.getName()))
                .findFirst();
    }

    @Override
    public boolean validateFilm(Film film) {
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
}
