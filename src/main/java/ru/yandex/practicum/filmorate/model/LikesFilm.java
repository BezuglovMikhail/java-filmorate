package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LikesFilm {
    private String filmId;
    private Set<String> userId;
}
