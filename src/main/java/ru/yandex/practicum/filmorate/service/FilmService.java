package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    private LikesStorage likesStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, LikesDbStorage likesDbStorage) {
        this.filmStorage = filmDbStorage;
        this.likesStorage = likesDbStorage;
    }

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = inMemoryUserStorage;
    }

    public Optional<Film> addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше: 28.12.1895");
        }
        return getFilmStorage().saveFilm(film);
    }

    public Film updateFilm(Film film) {
         getFilmStorage().updateFilm(film);
         Film filmGenres = filmStorage.findFilmById(film.getId());
         if (film.getGenres() == null) {
             filmGenres.setGenres(new HashSet<>());
         }
        return filmGenres;
    }

    public Film removeFilm(Film film) {
        return getFilmStorage().removeFilm(film);
    }

    public Film findByIdFilm(Long filmId) {
        return getFilmStorage().findFilmById(filmId);
    }

    public Collection<Film> findAllFilms() {
        return getFilmStorage().getFilms();
    }

    public void addLike(long filmId, long userId) {
        likesStorage.addLike(userId, filmId);
    }

    public void deleteLike(long userId, long filmId) {
        likesStorage.removeLike(userId, filmId);
    }

    public Collection<Film> findPopularFilms(Integer count) {
        return likesStorage.getPopular(count);
    }
}
