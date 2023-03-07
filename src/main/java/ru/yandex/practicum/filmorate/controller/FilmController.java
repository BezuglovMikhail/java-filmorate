package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    //private LocalDate MOVIE_BIRTHDAY = LocalDate.parse("28.12.1895");
  FilmRepository filmRepository = new FilmRepository();

    @GetMapping("/films")
    //@ResponseBody
    public Collection<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", filmRepository.getFilms().size());
        return filmRepository.getFilms().values();
    }

    @PostMapping(value = "films")
    public Film create(@Valid @RequestBody Film film) {


    /*if (user.getLogin().contains(" ")) {
        throw new ValidationException("Логин не может содержать пробелы.");
    }
    if (user.getName().isEmpty()) {
        user.setName(user.getLogin());
    }
    if (user.getBirthday().isBefore(LocalDateTime.now())) {
        throw new ValidationException("Дата рождения не может быть в будующем");
    }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }*/
        //if (validUser(film)) {

        //if (film.getReleaseDate().isBefore(LocalDate.parse("28.12.1895"))) {
           // throw new ValidationException("Дата релиза не может быть позже :" + LocalDate.parse("28.12.1895"));
        //}
        filmRepository.saveFilm(film);
        //}
        return film;
    }

    @PutMapping(value = "films")
    //@ResponseBody
    public Film appDate(@Valid @RequestBody Film film) {
        //if (validUser(film)) {
    if (!filmRepository.getFilms().containsKey(film.getId()) && film.getId() != 0) {
        throw new ValidationException("Пользователя с id = " + film.getId() + " нет.");
    }

            filmRepository.saveFilm(film);

        //}
        return film;
    }

    /*public boolean validUser(Film film) {
        if (film.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы.");
            //return false;
        }
        if (film.getBirthday().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата рождения не может быть в будующем");
            //return false;
        }
        if (film.getName().isEmpty()) {
            film.setFilm(film.getLogin());
            return true;
        }
        return true;
    }*/


}
