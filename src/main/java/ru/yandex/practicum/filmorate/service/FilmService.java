package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;

    private UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = inMemoryUserStorage;
    }

    public Optional<Film> addFilm(Film film) {
        return getFilmStorage().saveFilm(film);
    }

    public Film addLike(long filmId, long userId) {
        if (!getFilmStorage().getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
        if (!getUserStorage().getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден.", userId));
        }
        getFilmStorage().getFilms().get(filmId).getLikes().add(userId);
        return  getFilmStorage().getFilms().get(filmId);
    }

    public Collection<Film> findAllFilms(){
        return getFilmStorage().getFilms().values();
    }

    public Optional<Film> findByIdFilm(Long filmId){
        if (!getFilmStorage().getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
        return getFilmStorage().getFilms().values().stream()
                .filter(x -> x.getId() == filmId)
                .findFirst();
    }


    public void deleteLike(long userId, long filmId) {
        getFilmStorage().getFilms().get(filmId).getLikes().remove(userId);
    }

    public List<Film> findPopularFilms(Integer count) {
        return getFilmStorage().getFilms().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        if (p0.getLikes().size() == p1.getLikes().size()) {
            return 0;
        }
        if (p0.getLikes().size() < p1.getLikes().size()) {
            return 1;
        }
        if (p0.getLikes().size() > p1.getLikes().size()) {
            return -1;
        }
        return -2;
    }
}
