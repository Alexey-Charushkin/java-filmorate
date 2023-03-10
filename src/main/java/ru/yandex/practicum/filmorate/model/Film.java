package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Film extends Item {

    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MPARating mpaRating;
    private List<String> genreNames;
    private Integer rate = 0;

    @JsonIgnore
    private Set<Long> userFilmLikes = new HashSet<>();

    public void setUserAddLikeFilm(Long userId) {
        userFilmLikes.add(userId);
    }

    public void removeUserLikeFilm(Long userId) {
        userFilmLikes.remove(userId);
    }
}
