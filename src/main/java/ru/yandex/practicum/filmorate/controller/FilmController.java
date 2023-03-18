package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.FilmStorage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmStorage filmRepository = new InMemoryFilmStorage();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmRepository.getFilms().size());
        return filmRepository.getFilms().values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmRepository.saveFilm(film);
        log.debug("Добавлен фильм с id = {}", film.getId());
        return film;
    }

    @PutMapping
    public Film appDate(@Valid @RequestBody Film film) {
        if (!filmRepository.getFilms().containsKey(film.getId()) && film.getId() != 0) {
            throw new ValidationException("Фильма с id = " + film.getId() + " нет.");
        }
        filmRepository.saveFilm(film);
        log.debug("Обнавлён фильм с id = {}", film.getId());
        return film;
    }
}
