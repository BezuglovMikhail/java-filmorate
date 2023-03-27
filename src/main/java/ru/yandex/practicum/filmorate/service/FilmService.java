package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

    public long addLike(long userId, long filmId) {

        filmStorage.getFilms().get(filmId).getLikes().add(userId);
        return filmId;
    }

    public void deleteLike(long userId, long filmId) {
        filmStorage.getFilms().get(filmId).getLikes().remove(userId);
    }

    public List<Film> findPopularFilms(Integer count) {

        return filmStorage.getFilms().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        if (p0.getLikes().size() == p1.getLikes().size()) {
            return 0;
        }
        if (p0.getLikes().size() > p1.getLikes().size()) {
            return 1;
        }
        if (p0.getLikes().size() < p1.getLikes().size()) {
            return -1;
        }
        return -2;
    }
}
