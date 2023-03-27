package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmServiceTest {

    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    UserService userService = new UserService(userStorage);
    FilmService filmService = new FilmService(filmStorage);

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
    void addLike() {

        filmService.addLike(1, 1);
        filmService.addLike(2, 1);

        assertEquals(Set.of(1L, 2L), filmStorage.getFilms().get(1L).getLikes());
        assertEquals(2, filmStorage.getFilms().get(1L).getLikes().size());
    }

    @Test
    void deleteLike() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(3, 1);
        filmService.addLike(4, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 2);

        filmService.deleteLike(1, 1);
        filmService.deleteLike(2, 2);
        filmService.deleteLike(3, 2);

        assertEquals(Set.of(2L, 3L, 4L), filmStorage.getFilms().get(1L).getLikes());
        assertEquals(Set.of(), filmStorage.getFilms().get(2L).getLikes());
    }

    @Test
    void findPopularFilms() {
        filmService.addLike(1, 10);
        filmService.addLike(2, 10);
        filmService.addLike(3, 10);
        filmService.addLike(4, 10);
        filmService.addLike(2, 2);
        filmService.addLike(3, 2);
        filmService.addLike(1, 1);
        filmService.addLike(2, 3);
        filmService.addLike(3, 4);
        filmService.addLike(4, 5);
        filmService.addLike(2, 6);
        filmService.addLike(3, 7);
        filmService.addLike(4, 7);
        filmService.addLike(1, 8);
        filmService.addLike(2, 8);
        filmService.addLike(3, 8);
        filmService.addLike(4, 9);

        assertEquals(List.of(filmService.getFilmStorage().getFilms().get(10L),
                        filmService.getFilmStorage().getFilms().get(8L),
                        filmService.getFilmStorage().getFilms().get(2L),
                        filmService.getFilmStorage().getFilms().get(7L)),
                filmService.findPopularFilms(4));
        assertEquals(List.of(filmService.getFilmStorage().getFilms().get(10L),
                filmService.getFilmStorage().getFilms().get(8L),
                filmService.getFilmStorage().getFilms().get(2L),
                filmService.getFilmStorage().getFilms().get(7L),
                filmService.getFilmStorage().getFilms().get(1L),
                filmService.getFilmStorage().getFilms().get(3L),
                filmService.getFilmStorage().getFilms().get(4L),
                filmService.getFilmStorage().getFilms().get(5L),
                filmService.getFilmStorage().getFilms().get(6L),
                filmService.getFilmStorage().getFilms().get(9L)), filmService.findPopularFilms(10));
        assertEquals(List.of(filmService.getFilmStorage().getFilms().get(10L),
                filmService.getFilmStorage().getFilms().get(8L),
                filmService.getFilmStorage().getFilms().get(2L),
                filmService.getFilmStorage().getFilms().get(7L),
                filmService.getFilmStorage().getFilms().get(1L),
                filmService.getFilmStorage().getFilms().get(3L),
                filmService.getFilmStorage().getFilms().get(4L),
                filmService.getFilmStorage().getFilms().get(5L),
                filmService.getFilmStorage().getFilms().get(6L),
                filmService.getFilmStorage().getFilms().get(9L),
                filmService.getFilmStorage().getFilms().get(11L)), filmService.findPopularFilms(11));
    }
}
