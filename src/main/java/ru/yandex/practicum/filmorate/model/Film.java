package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class Film {
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Set<Long> likes = new LinkedHashSet<>();
    private Set<Genre> genres;
    @Positive
    private int duration;
    //@NotNull
    private MPA mpa;
    private Long rate;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration,  Set<Genre> genres, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration,  Set<Genre> genres, MPA mpa, long rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
        this.rate = rate;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        //values.put("mpa_id", mpa);
        return values;
    }
    //private Set<Long> likes = new HashSet<>();
}

/*import lombok.Data;

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
}*/
