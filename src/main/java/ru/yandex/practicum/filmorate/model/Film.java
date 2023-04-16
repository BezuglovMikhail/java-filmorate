package ru.yandex.practicum.filmorate.model;

/*import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Long> likes = new HashSet<>();
}*/

import lombok.Data;

@Data
public class Film {
    private String filmId;

    //@NotNull
    //@NotBlank
    private String filmName;

    //@Size(max = 200)
    private String filmDescription;
    private String filmReleaseDate;

    //@Positive
    private String filmDuration;

    private String filmRating;

    //private Set<Long> likes = new HashSet<>();
}
