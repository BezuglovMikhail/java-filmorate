package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikesStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;

    private LikesStorage likesStorage;

    @Autowired
    //public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
   //     this.filmStorage = inMemoryFilmStorage;
   //     this.userStorage = inMemoryUserStorage;
    public FilmService(FilmDbStorage filmDbStorage, LikesDbStorage likesDbStorage) {
        this.filmStorage = filmDbStorage;
        this.likesStorage = likesDbStorage;
   }

    public Optional<Film> addFilm(Film film) {
        return getFilmStorage().saveFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return getFilmStorage().updateFilm(film);
    }

    public Film removeFilm(Film film) {
        return getFilmStorage().removeFilm(film);
    }

    public Optional<Film> findByIdFilm(Long filmId) {
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
