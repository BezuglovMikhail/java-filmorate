package ru.yandex.practicum.filmorate.constants;

import lombok.Getter;

public enum Genres {
    БОЕВИКИ(1),
    ВЕСТЕРНЫ(2),
    ДЕТЕКТИЫ(3),
    ИСТОРИЧЕСКИЕ(4),
    ДРАМЫ(5),
    КОММЕДИИ(6),
    МЕЛОДРАМЫ(7),
    ПРИКЛЮЧЕНИЯ(8),
    ТРИЛЛЕРЫ(9),
    УЖАСЫ(10),
    ФАНТАСТИКА(11);

    @Getter
    private final int genreId;

    Genres(int genreId) {
        this.genreId = genreId;
    }
}
