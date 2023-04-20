package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        if (validateFilm(film.getId())) {
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
    public Optional<Film> updateFilm(Film film) {
        if (validateFilm(film.getId())) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            }
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
            throw new FilmNotFoundException("Фильма с id = " + filmId + " нет.");
        }
        return true;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
        return Optional.ofNullable(films.get(filmId));
    }

    public Film addLike(long filmId, long userId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
       // if (!users.containsKey(userId)) {
       //    throw new UserNotFoundException(String.format("Пользователь с id = %s не найден.", userId));
        //}
        films.get(filmId).getLikes().add(userId);
        return films.get(filmId);
    }

    public void deleteLike(long userId, long filmId) {
        // getFilmStorage().getFilms().get(filmId).getLikes().remove(userId);
    }

    public List<Film> findPopularFilms(Integer count) {
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
