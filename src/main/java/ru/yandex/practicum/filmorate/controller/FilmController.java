package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.getFilmStorage().getFilms().size());
        return filmService.getFilmStorage().getFilms().values();
    }

    @GetMapping("/{filmId}")
    public Optional<Film> findById(@PathVariable long filmId) {
        return filmService.getFilmStorage().getFilms().values().stream()
                .filter(x -> x.getId() == filmId)
                .findFirst();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmService.getFilmStorage().saveFilm(film);
        log.debug("Добавлен фильм с id = {}", film.getId());
        return filmService.getFilmStorage().getFilms().get(film.getId());
    }

    @PutMapping
    public Film appDate(@Valid @RequestBody Film film) {
        if (!filmService.getFilmStorage().getFilms().containsKey(film.getId()) && film.getId() != 0) {
            throw new ValidationException("Фильма с id = " + film.getId() + " нет.");
        }
        filmService.getFilmStorage().saveFilm(film);
        log.debug("Обнавлён фильм с id = {}", film.getId());
        return filmService.getFilmStorage().getFilms().get(film.getId());
    }

    @PutMapping ("/{id}/like/{userId}")
    public Film addFriends(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "userId") Long userId
    ) {
        filmService.addLike(id, userId);
        log.debug("Пользователь с id = {userId} поставил лайк фильму с id = {id}");
        return filmService.getFilmStorage().getFilms().get(id);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> findPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {

        if (count <= 0) {
            throw new IllegalArgumentException();
        }
        return filmService.findPopularFilms(count);
    }
}
