package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikesDbStorageTest {
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private final LikesDbStorage likesStorage;



    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void addLike() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.saveUser(user2);

        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, "G"));

        filmStorage.saveFilm(film1);

        Film film2 = new Film();
        film2.setName("Deadpool 3,5");
        film2.setDescription("Скоро выйдет...)");
        film2.setReleaseDate(LocalDate.of(2025, 11, 6));
        film2.setDuration(180);
        film2.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film2);

        likesStorage.addLike(1L, 2L);
        likesStorage.addLike(2L, 2L);
        film2.setRate(2L);

        HashMap<Long, Film> filmsTest = new HashMap();
        filmsTest.put(1L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                likesStorage.getPopular(1).stream().collect(Collectors.toList()));
        assertEquals(2, likesStorage.getPopular(2).size());

    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void removeLike() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.saveUser(user2);

        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, "G"));

        filmStorage.saveFilm(film1);

        Film film2 = new Film();
        film2.setName("Deadpool 3,5");
        film2.setDescription("Скоро выйдет...)");
        film2.setReleaseDate(LocalDate.of(2025, 11, 6));
        film2.setDuration(180);
        film2.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film2);

        likesStorage.addLike(1L, 2L);
        likesStorage.addLike(2L, 2L);
        likesStorage.addLike(2L, 1L);
        likesStorage.removeLike(2L, 1L);
        likesStorage.removeLike(2L, 2L);
        film1.setRate(1L);

        HashMap<Long, Film> filmsTest = new HashMap();
        filmsTest.put(1L, film1);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                likesStorage.getPopular(1).stream().collect(Collectors.toList()));
        assertEquals(2, likesStorage.getPopular(2).size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getPopular() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.saveUser(user2);

        User user3 = new User();
        user3.setEmail("mail_test2@mail.ru");
        user3.setName("Logan");
        user3.setLogin("Log_test");
        user3.setBirthday(LocalDate.of(1998, 9, 7));

        userStorage.saveUser(user3);

        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, "G"));

        filmStorage.saveFilm(film1);

        Film film2 = new Film();
        film2.setName("Deadpool 2");
        film2.setDescription("Класс...)");
        film2.setReleaseDate(LocalDate.of(2015, 7, 6));
        film2.setDuration(180);
        film2.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film2);

        Film film3 = new Film();
        film3.setName("Deadpool");
        film3.setDescription("Схожу ещё!!!");
        film3.setReleaseDate(LocalDate.of(2010, 7, 6));
        film3.setDuration(180);
        film3.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film3);

        likesStorage.addLike(1L, 3L);
        likesStorage.addLike(2L, 3L);
        likesStorage.addLike(3L, 3L);
        likesStorage.addLike(2L, 2L);
        likesStorage.addLike(1L, 1L);
        likesStorage.addLike(2L, 1L);

        film1.setRate(2L);
        film2.setRate(1L);
        film3.setRate(3L);

        LinkedHashMap<Long, Film> filmsTest = new LinkedHashMap();
        filmsTest.put(1L, film3);
        filmsTest.put(2L, film1);
        filmsTest.put(3L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                likesStorage.getPopular(3).stream().collect(Collectors.toList()));
        assertEquals(3, likesStorage.getPopular(3).size());
    }
}