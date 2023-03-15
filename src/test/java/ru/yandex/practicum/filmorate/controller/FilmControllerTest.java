package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController = new FilmController();

    @Test
    void createTrueTest() {
        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmController.create(film);
        assertEquals(1, filmController.filmRepository.getFilms().get(1).getId());
        assertEquals("Deadpool 3", filmController.filmRepository.getFilms().get(1).getName());
        assertEquals("Скоро выйдет)", filmController.filmRepository.getFilms().get(1).getDescription());
        assertEquals(190, filmController.filmRepository.getFilms().get(1).getDuration());
        assertEquals(LocalDate.of(2024, 11, 6),
                filmController.filmRepository.getFilms().get(1).getReleaseDate());
    }

    @Test
    void createFalse1Test() {
        Film film = new Film();
        film.setName("Начало");
        film.setDescription("Ждем ошибку)");
        film.setReleaseDate(LocalDate.of(1894, 11, 6));
        film.setDuration(90);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                filmController.create(film);
            }
        });

        assertEquals("Дата релиза не может быть раньше: 28.12.1895", ex.getMessage());
    }

    @Test
    void appDateTrueTest() {

        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmController.create(film);

        Film filmAppDate = new Film();
        filmAppDate.setId(1);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);

        filmController.appDate(filmAppDate);

        assertEquals(1, filmController.filmRepository.getFilms().get(1).getId());
        assertEquals("Deadpool 3,5", filmController.filmRepository.getFilms().get(1).getName());
        assertEquals("Скоро выйдет...)", filmController.filmRepository.getFilms().get(1).getDescription());
        assertEquals(180, filmController.filmRepository.getFilms().get(1).getDuration());
        assertEquals(LocalDate.of(2025, 11, 6),
                filmController.filmRepository.getFilms().get(1).getReleaseDate());
    }

    @Test
    void appDateFalseTest() {

        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmController.create(film);

        Film filmAppDate = new Film();
        filmAppDate.setId(100);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                filmController.appDate(filmAppDate);
            }
        });

        assertEquals("Фильма с id = 100 нет.", ex.getMessage());
    }

    @Test
    void findAllTest() {
        assertEquals(0, filmController.findAll().size());
    }


    @Test
    void findAll1Test() {

        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmController.create(film);

        Film film2 = new Film();
        film2.setName("Начало");
        film2.setDescription("Ждем ошибку)");
        film2.setReleaseDate(LocalDate.of(1994, 11, 6));
        film2.setDuration(90);

        filmController.create(film2);

        HashMap<Integer, Film> filmsTest = new HashMap<>();
        filmsTest.put(1, film);
        filmsTest.put(2, film2);

        assertEquals(filmsTest, filmController.filmRepository.getFilms());
        assertEquals(2, filmController.findAll().size());
    }
}