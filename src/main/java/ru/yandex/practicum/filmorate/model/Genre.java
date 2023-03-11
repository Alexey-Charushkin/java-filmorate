package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public
class Genre {
    private int id;
    private Genres genre;
}

enum Genres {КОМЕДИЯ, ДРАМА, МУЛЬТФИЛЬМ, ТРИЛЛЕР, ДОКУМЕНТАЛЬНЫЙ, БОЕВИК}