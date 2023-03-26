package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;

    @Positive
    private int duration;

    Set<Long> likes = new HashSet<>();
}
