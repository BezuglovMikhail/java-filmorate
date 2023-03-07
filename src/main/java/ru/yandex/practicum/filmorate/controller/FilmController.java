package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
public class FilmController {
    FilmRepository filmRepository = new FilmRepository();

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", filmRepository.getFilms().size());
        return filmRepository.getFilms().values();
    }

    @PostMapping(value = "films")
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть позже :" + LocalDate.parse("28.12.1895"));
        }
        filmRepository.saveFilm(film);
        return film;
    }

    @PutMapping(value = "films")
    public Film appDate(@Valid @RequestBody Film film) {
        if (!filmRepository.getFilms().containsKey(film.getId()) && film.getId() != 0) {
            throw new ValidationException("Пользователя с id = " + film.getId() + " нет.");
        }
        filmRepository.saveFilm(film);
        return film;
    }
}
