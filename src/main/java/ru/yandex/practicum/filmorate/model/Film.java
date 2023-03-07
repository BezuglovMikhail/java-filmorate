package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull
    @NotBlank
    private String name;

    //@Max(200)
    private String description;

    @Past
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
