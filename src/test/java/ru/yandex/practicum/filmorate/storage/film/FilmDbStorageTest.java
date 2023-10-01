package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Test
    void saveAndFindByIdFilm() {
        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, null));

        filmStorage.saveFilm(film1);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Deadpool 3")
                                .hasFieldOrPropertyWithValue("description", "Скоро выйдет)")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2024, 11, 6))
                                .hasFieldOrPropertyWithValue("duration", 190)
                                .hasFieldOrPropertyWithValue("mpa.id", 1)
                                .hasFieldOrPropertyWithValue("mpa.name", "G")
                );
    }

    @Test
    void updateFilm() {
        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, null));

        filmStorage.saveFilm(film1);

        Film filmAppDate = new Film();
        filmAppDate.setId(1L);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);
        filmAppDate.setMpa(new MPA(4, null));

        filmStorage.updateFilm(filmAppDate);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Deadpool 3,5")
                                .hasFieldOrPropertyWithValue("description", "Скоро выйдет...)")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2025, 11, 6))
                                .hasFieldOrPropertyWithValue("duration", 180)
                                .hasFieldOrPropertyWithValue("mpa.id", 4)
                                .hasFieldOrPropertyWithValue("mpa.name", "R")
                );
    }

    @Test
    void removeFilm() {
        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(1900, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, null));

        filmStorage.saveFilm(film1);

        Film film2 = new Film();
        film2.setName("Deadpool 3,5");
        film2.setDescription("Скоро выйдет...)");
        film2.setReleaseDate(LocalDate.of(2025, 11, 6));
        film2.setDuration(180);
        film2.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film2);

        filmStorage.removeFilm(film1);

        HashMap<Long, Film> filmsTest = new HashMap();
        filmsTest.put(2L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                filmStorage.getFilms().stream().collect(Collectors.toList()));
        assertEquals(1, filmStorage.getFilms().size());

    }

    @Test
    void validateFalseIdFilm() {
        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(1900, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, null));

        filmStorage.saveFilm(film1);

        Film filmAppDate = new Film();
        filmAppDate.setId(1000L);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);
        filmAppDate.setMpa(new MPA(4, null));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                filmStorage.updateFilm(filmAppDate);
            }
        });

        assertEquals("Фильма с id = 1000 нет.", ex.getMessage());
    }

    @Test
    void getFilms() {

        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(1, "G"));

        filmStorage.saveFilm(film1);

        Film film2 = new Film();
        film2.setName("Начало");
        film2.setDescription("Ждем ошибку)");
        film2.setReleaseDate(LocalDate.of(1994, 11, 6));
        film2.setDuration(90);
        film2.setMpa(new MPA(5, "NC-17"));

        filmStorage.saveFilm(film2);

        HashMap<Long, Film> filmsTest = new HashMap();
        filmsTest.put(1L, film1);
        filmsTest.put(2L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                filmStorage.getFilms().stream().collect(Collectors.toList()));
        assertEquals(2, filmStorage.getFilms().size());
    }

    @Test
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

        filmStorage.addLike(1L, 2L);
        filmStorage.addLike(2L, 2L);
        film2.setRate(2L);

        HashMap<Long, Film> filmsTest = new HashMap();
        filmsTest.put(1L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                filmStorage.getPopular(1).stream().collect(Collectors.toList()));
        assertEquals(2, filmStorage.getPopular(2).size());

    }

    @Test
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

        filmStorage.addLike(1L, 2L);
        filmStorage.addLike(2L, 2L);
        filmStorage.addLike(2L, 1L);
        filmStorage.removeLike(2L, 1L);
        filmStorage.removeLike(2L, 2L);
        film1.setRate(1L);

        HashMap<Long, Film> filmsTest = new HashMap();
        filmsTest.put(1L, film1);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                filmStorage.getPopular(1).stream().collect(Collectors.toList()));
        assertEquals(2, filmStorage.getPopular(2).size());
    }

    @Test
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
        user3.setLogin("Log_test2");
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

        filmStorage.addLike(1L, 3L);
        filmStorage.addLike(2L, 3L);
        filmStorage.addLike(3L, 3L);
        filmStorage.addLike(2L, 2L);
        filmStorage.addLike(1L, 1L);
        filmStorage.addLike(2L, 1L);

        film1.setRate(2L);
        film2.setRate(1L);
        film3.setRate(3L);

        LinkedHashMap<Long, Film> filmsTest = new LinkedHashMap();
        filmsTest.put(1L, film3);
        filmsTest.put(2L, film1);
        filmsTest.put(3L, film2);

        assertEquals(filmsTest.values().stream().collect(Collectors.toList()),
                filmStorage.getPopular(3).stream().collect(Collectors.toList()));
        assertEquals(3, filmStorage.getPopular(3).size());
    }
}
