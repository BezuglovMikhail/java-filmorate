package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FilmStorageTest {

    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    @Test
    void generateIdTest() {
        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmStorage.saveFilm(film);

        assertEquals(1, filmStorage.findFilmById(1L).getId());
        assertEquals("Deadpool 3", filmStorage.findFilmById(1L).getName());
        assertEquals("Скоро выйдет)", filmStorage.findFilmById(1L).getDescription());
        assertEquals(190, filmStorage.findFilmById(1L).getDuration());
        assertEquals(LocalDate.of(2024, 11, 6), filmStorage.findFilmById(1L).getReleaseDate());
    }

    @Test
    void saveFilmFalseTest() {
        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmStorage.saveFilm(film);

        Film filmAppDate = new Film();
        filmAppDate.setId(100);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                filmStorage.updateFilm(filmAppDate);
            }
        });

        assertEquals("Фильма с id = 100 нет.", ex.getMessage());
    }

    @Test
    void saveFilmAppDateTrueTest() {
        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmStorage.saveFilm(film);

        Film filmAppDate = new Film();
        filmAppDate.setId(1);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);

        filmStorage.updateFilm(filmAppDate);

        assertEquals(1, filmStorage.findFilmById(1L).getId());
        assertEquals("Deadpool 3,5", filmStorage.findFilmById(1L).getName());
        assertEquals("Скоро выйдет...)", filmStorage.findFilmById(1L).getDescription());
        assertEquals(180, filmStorage.findFilmById(1L).getDuration());
        assertEquals(LocalDate.of(2025, 11, 6), filmStorage.findFilmById(1L).getReleaseDate());
    }

    @Test
    void validateFalseTest() {
        Film film = new Film();
        film.setName("Начало");
        film.setDescription("Ждем ошибку)");
        film.setReleaseDate(LocalDate.of(1894, 11, 6));
        film.setDuration(90);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                filmStorage.saveFilm(film);
            }
        });

        assertEquals("Дата релиза не может быть раньше: 28.12.1895", ex.getMessage());
    }

    @Test
    void getFilmsTest() {
        assertEquals(0, filmStorage.getFilms().size());
    }

    @Test
    void getFilms2Test() {
        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        filmStorage.saveFilm(film);

        Film film2 = new Film();
        film2.setName("Начало");
        film2.setDescription("Ждем ошибку)");
        film2.setReleaseDate(LocalDate.of(1994, 11, 6));
        film2.setDuration(90);

        filmStorage.saveFilm(film2);

        HashMap<Long, Film> filmsTest = new HashMap<>();
        filmsTest.put(1L, film);
        filmsTest.put(2L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()), filmStorage.getFilms().stream().collect(Collectors.toList()));
        assertEquals(2, filmStorage.getFilms().size());
    }
}
