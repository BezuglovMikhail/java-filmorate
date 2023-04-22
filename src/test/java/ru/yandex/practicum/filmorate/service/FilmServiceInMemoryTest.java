package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmServiceInMemoryTest {

    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(filmStorage, userStorage);

    @BeforeEach
    void createUsersFilmsForTest() throws IOException, InterruptedException {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        User user2 = new User();
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(1988, 1, 16));

        User user3 = new User();
        user2.setEmail("mailrtyj@mail.ru");
        user2.setName("Nikyfk");
        user2.setLogin("Login678");
        user2.setBirthday(LocalDate.of(2005, 5, 26));

        User user4 = new User();
        user2.setEmail("mailrtweryyj@mail.ru");
        user2.setName("Nikyfkeryh");
        user2.setLogin("Login678678");
        user2.setBirthday(LocalDate.of(2001, 10, 30));
        userStorage.saveUser(user);
        userStorage.saveUser(user2);
        userStorage.saveUser(user3);
        userStorage.saveUser(user4);

        Film film = new Film();
        film.setName("Deadpool 3");
        film.setDescription("Скоро выйдет)");
        film.setReleaseDate(LocalDate.of(2024, 11, 6));
        film.setDuration(190);

        Film film2 = new Film();
        film2.setName("Начало");
        film2.setDescription("Ждем ошибку)");
        film2.setReleaseDate(LocalDate.of(1994, 11, 6));
        film2.setDuration(90);

        Film film3 = new Film();
        film3.setName("rshrsh");
        film3.setDescription("dthjrtjryjkryk)");
        film3.setReleaseDate(LocalDate.of(1945, 11, 6));
        film3.setDuration(90);

        Film film4 = new Film();
        film4.setName("rtju");
        film4.setDescription("rtuirtyiik)");
        film4.setReleaseDate(LocalDate.of(2045, 11, 6));
        film4.setDuration(90);

        Film film5 = new Film();
        film5.setName("rtyjktyi7i");
        film5.setDescription("t6yietieio769io)");
        film5.setReleaseDate(LocalDate.of(1999, 11, 6));
        film5.setDuration(90);

        Film film6 = new Film();
        film6.setName("Начало");
        film6.setDescription("Ждем ошибку)");
        film6.setReleaseDate(LocalDate.of(2003, 11, 6));
        film6.setDuration(90);

        Film film7 = new Film();
        film7.setName("rtjktyuk8yui");
        film7.setDescription("rtysirityi)");
        film7.setReleaseDate(LocalDate.of(2004, 11, 6));
        film7.setDuration(90);

        Film film8 = new Film();
        film8.setName("rtjktyuk8yui");
        film8.setDescription("rtysirityi)");
        film8.setReleaseDate(LocalDate.of(2005, 11, 6));
        film8.setDuration(90);

        Film film9 = new Film();
        film9.setName("rtjktyuk8yui");
        film9.setDescription("rtysirityi)");
        film9.setReleaseDate(LocalDate.of(2006, 11, 6));
        film9.setDuration(90);

        Film film10 = new Film();
        film10.setName("rtjktrikyyuk8yui");
        film10.setDescription("rtysrkyirityi)");
        film10.setReleaseDate(LocalDate.of(2007, 11, 6));
        film10.setDuration(90);

        Film film11 = new Film();
        film11.setName("rtjktyukfyk8yui");
        film11.setDescription("rtysir6yirityi)");
        film11.setReleaseDate(LocalDate.of(2235, 11, 6));
        film11.setDuration(90);

        filmStorage.saveFilm(film);
        filmStorage.saveFilm(film2);
        filmStorage.saveFilm(film3);
        filmStorage.saveFilm(film4);
        filmStorage.saveFilm(film5);
        filmStorage.saveFilm(film6);
        filmStorage.saveFilm(film7);
        filmStorage.saveFilm(film8);
        filmStorage.saveFilm(film9);
        filmStorage.saveFilm(film10);
        filmStorage.saveFilm(film11);
    }

    @Test
    void addFilmTest() {
        Film film12 = new Film();
        film12.setName("Пушка");
        film12.setDescription("........");
        film12.setReleaseDate(LocalDate.of(2235, 11, 6));
        film12.setDuration(90);

        filmService.addFilm(film12);

        film12.setId(12);
        assertEquals(film12, filmStorage.findFilmById(12L));
    }

    @Test
    void addFilmAndAppDateTest() {
        Film film12 = new Film();
        film12.setName("Пушка");
        film12.setDescription("........");
        film12.setReleaseDate(LocalDate.of(2235, 11, 6));
        film12.setDuration(90);

        Film film12AppDate = new Film();
        film12AppDate.setName("Пушка - гонка");
        film12AppDate.setDescription("Проверка");
        film12AppDate.setReleaseDate(LocalDate.of(2013, 10, 13));
        film12AppDate.setDuration(95);
        film12AppDate.setId(12);

        filmStorage.saveFilm(film12);
        filmStorage.saveFilm(film12AppDate);

        assertEquals(film12AppDate, filmStorage.findFilmById (12L));
    }

    @Test
    void addLikeTest() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);

        assertEquals(Set.of(1L, 2L), filmStorage.findFilmById (1L).getLikes());
        assertEquals(2, filmStorage.findFilmById (1L).getLikes().size());
    }

    @Test
    void addLikeFalseFilmTest() {
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                filmStorage.addLike(-1, 1);
            }
        });

        assertEquals("Фильм с id = -1 не найден.", ex.getMessage());
    }

    @Test
    void deleteLikeTest() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);
        filmStorage.addLike(1, 4);
        filmStorage.addLike(2, 2);
        filmStorage.addLike(2, 3);

        filmStorage.deleteLike(1, 1);
        filmStorage.deleteLike(2, 2);
        filmStorage.deleteLike(3, 2);

        assertEquals(Set.of(2L, 3L, 4L), filmStorage.findFilmById (1L).getLikes());
        assertEquals(Set.of(), filmStorage.findFilmById (2L).getLikes());
    }

    @Test
    void findPopularFilmsTest() {
        filmStorage.addLike(10, 1);
        filmStorage.addLike(10, 2);
        filmStorage.addLike(10, 3);
        filmStorage.addLike(10, 4);
        filmStorage.addLike(2, 2);
        filmStorage.addLike(2, 3);
        filmStorage.addLike(1, 1);
        filmStorage.addLike(3, 2);
        filmStorage.addLike(4, 3);
        filmStorage.addLike(5, 4);
        filmStorage.addLike(6, 2);
        filmStorage.addLike(7, 3);
        filmStorage.addLike(7, 4);
        filmStorage.addLike(8, 1);
        filmStorage.addLike(8, 2);
        filmStorage.addLike(8, 3);
        filmStorage.addLike(9, 4);

        LinkedHashMap<Long, Film> popularFilmsTest = new LinkedHashMap<>();
        popularFilmsTest.put(1L, filmStorage.findFilmById(10L));
        popularFilmsTest.put(2L, filmStorage.findFilmById(8L));
        popularFilmsTest.put(3L, filmStorage.findFilmById(2L));
        popularFilmsTest.put(4L, filmStorage.findFilmById(7L));

        assertEquals(popularFilmsTest.values().stream().collect(Collectors.toList()),
                filmStorage.findPopularFilms(4));

    }

    @Test
    void findAllFilmsTest() {
        Film film12 = new Film();
        film12.setName("Пушка");
        film12.setDescription("........");
        film12.setReleaseDate(LocalDate.of(2235, 11, 6));
        film12.setDuration(90);

        Film film13 = new Film();
        film13.setName("Пушка - гонка");
        film13.setDescription("Проверка");
        film13.setReleaseDate(LocalDate.of(2013, 10, 13));
        film13.setDuration(95);
        film13.setId(12);

        filmStorage.saveFilm(film12);
        filmStorage.saveFilm(film13);

        assertEquals(filmStorage.getFilms(), filmService.findAllFilms());
    }

    @Test
    void findByIdFilmTest() {
        Film film12 = new Film();
        film12.setName("Пушка");
        film12.setDescription("........");
        film12.setReleaseDate(LocalDate.of(2235, 11, 6));
        film12.setDuration(90);

        Film film13 = new Film();
        film13.setName("Пушка - гонка");
        film13.setDescription("Проверка");
        film13.setReleaseDate(LocalDate.of(2013, 10, 13));
        film13.setDuration(95);

        filmStorage.saveFilm(film12);
        filmStorage.saveFilm(film13);

        assertEquals(film13, filmService.findByIdFilm(13L));
    }

    @Test
    void findByIdFilmFalseTest() {
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
        @Override
        public void execute() throws IOException {
            filmService.findByIdFilm(35L);
        }
    });

    assertEquals("Фильм с id = 35 не найден.", ex.getMessage());
    }
}
