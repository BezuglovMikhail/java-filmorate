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

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
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
    @Sql({"/schema.sql", "/data.sql"})
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
    @Sql({"/schema.sql", "/data.sql"})
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
    @Sql({"/schema.sql", "/data.sql"})
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
    @Sql({"/schema.sql", "/data.sql"})
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
}
