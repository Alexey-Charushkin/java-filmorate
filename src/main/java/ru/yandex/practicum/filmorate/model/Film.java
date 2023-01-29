package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class Film {

    private Integer id;

    @NonNull
    @NotBlank
    private String name;

    private String description;

    private LocalDate releaseDate;

    private Duration duration;

    public Film(String name, String description, String releaseDate, String duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = Duration.ofMinutes(Long.parseLong(duration));
    }
}
