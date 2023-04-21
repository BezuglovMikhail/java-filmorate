package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.findAllFilms().size());
        return filmService.findAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable Long filmId) {
        return filmService.findByIdFilm(filmId);
    }

    @PostMapping
    public Optional<Film> create(@Valid @RequestBody Film film) {
        //log.debug("Добавлен фильм с id = {}", film.getId());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        //log.debug("Обновлён фильм с id = {}", film.getId());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addFriends(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "userId") Long userId
    ) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dellFriends(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "userId") Long userId
    ) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IllegalArgumentException();
        }
        return filmService.findPopularFilms(count);
    }
}
