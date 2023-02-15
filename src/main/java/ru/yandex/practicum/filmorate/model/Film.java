package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
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

    private Integer rate = 0;
    private Set<Long> userFilmLikes = new HashSet<>();
    public void setUserAddLikeFilm(Long userId) { userFilmLikes.add(userId); }

    public Set<Long> getUserFilmLikes() { return userFilmLikes; }

    public void removeUserLikeFilm(Long userId) {
        userFilmLikes.remove(userId);
    }
}
