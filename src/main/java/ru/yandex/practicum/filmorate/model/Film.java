package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

@Getter
@Setter
@ToString
@FieldDefaults(level=AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Film extends Item {

    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Integer rate = 0;
    MPA mpa;
    List<Genre> genres;

    @JsonIgnore
    private Set<Long> userFilmLikes = new HashSet<>();

    public void setUserAddLikeFilm(Long userId) {
        userFilmLikes.add(userId);
    }

    public void removeUserLikeFilm(Long userId) {
        userFilmLikes.remove(userId);
    }
}
