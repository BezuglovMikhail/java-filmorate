package ru.yandex.practicum.filmorate.constants;

import lombok.Getter;

public enum Rating {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
    NC_17(5);

    @Getter
    private final int ratingId;

    Rating(int ratingId) {
        this.ratingId = ratingId;
    }
}
