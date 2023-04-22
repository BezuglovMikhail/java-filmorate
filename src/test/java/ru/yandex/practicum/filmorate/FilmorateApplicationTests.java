package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {

        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);


        Optional<User> userOptional = userStorage.findUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateUser() {

        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.updateUser(user2);

        Optional<User> userOptional = userStorage.findUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("email", "mail_test@mail.ru")
                        .hasFieldOrPropertyWithValue("name", "Log")
                        .hasFieldOrPropertyWithValue("login", "Log_test")
                        .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1995, 11, 7))
                );
    }

    @Test
    public void testAddFilm() {

        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film1);

        Optional<Film> filmTest = Optional.ofNullable(filmStorage.findFilmById(1L));

        assertThat(filmTest)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "Deadpool 3")
                        .hasFieldOrPropertyWithValue("description", "Скоро выйдет)")
                        .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2024, 11, 6))
                        .hasFieldOrPropertyWithValue("duration", 190)
                );
    }

    @Test
    public void testUpdateFilm() {

        Film film1 = new Film();
        film1.setName("Deadpool 3");
        film1.setDescription("Скоро выйдет)");
        film1.setReleaseDate(LocalDate.of(2024, 11, 6));
        film1.setDuration(190);
        film1.setMpa(new MPA(4, "R"));

        filmStorage.saveFilm(film1);

        Film filmAppDate = new Film();
        filmAppDate.setId(1L);
        filmAppDate.setName("Deadpool 3,5");
        filmAppDate.setDescription("Скоро выйдет...)");
        filmAppDate.setReleaseDate(LocalDate.of(2025, 11, 6));
        filmAppDate.setDuration(180);
        filmAppDate.setMpa(new MPA(4, "R"));

        filmStorage.updateFilm(filmAppDate);

        Optional<Film> filmTest = Optional.ofNullable(filmStorage.findFilmById(1L));

        assertThat(filmTest)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "Deadpool 3,5")
                        .hasFieldOrPropertyWithValue("description", "Скоро выйдет...)")
                        .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2025, 11, 6))
                        .hasFieldOrPropertyWithValue("duration", 180)
                );
    }
}
