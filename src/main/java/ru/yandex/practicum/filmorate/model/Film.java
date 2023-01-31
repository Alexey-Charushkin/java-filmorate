package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Film extends Item {

    @NonNull
    @NotBlank
    private String name;

    private String description;

    private LocalDate releaseDate;

    private Duration duration;

}
