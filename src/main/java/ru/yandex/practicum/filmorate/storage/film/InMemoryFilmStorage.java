package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long idFilm = 0;

    @Override
    public long generateId() {
        return ++idFilm;
    }

    @Override
    public Optional<Film> saveFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше: 28.12.1895");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            film.setId(generateId());
            films.put(film.getId(), film);
        }

        return films.values().stream()
                .filter(x -> Objects.equals(x.getName(), film.getName()))
                .findFirst();
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше: 28.12.1895");
        }
        if (!films.containsKey(film.getId()) && film.getId() != 0) {
            throw new NotFoundException("Фильма с id = " + film.getId() + " нет.");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }

        return films.values().stream()
                .filter(x -> Objects.equals(x.getName(), film.getName()))
                .findFirst();
    }

    @Override
    public Film removeFilm(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public boolean validateFilm(Long filmId) {
        if (films.get(filmId).getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше: 28.12.1895");
        }
        if (!films.containsKey(filmId) && filmId != 0) {
            throw new NotFoundException("Фильма с id = " + filmId + " нет.");
        }
        return true;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film findFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
        return films.get(filmId);
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return films.values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        if (p0.getLikes().size() == p1.getLikes().size()) {
            return 0;
        }
        if (p0.getLikes().size() < p1.getLikes().size()) {
            return 1;
        }
        if (p0.getLikes().size() > p1.getLikes().size()) {
            return -1;
        }
        return -2;
    }
}
