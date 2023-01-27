package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode

public class Film {

    private Integer filmId;

    private String filmName;

    private String filmDescription;

    private LocalDate releaseDate;

    private Duration filmDuration;

}
