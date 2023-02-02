package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Film extends Item {
    @NonNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
