package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
class Genre {
    private Genres genre;
}

enum Genres {КОИЕДИЯ, ДРАМА, МУЛЬТФИЛЬМ, ТРИЛЛЕР, ДОКУМЕНТАЛЬНЫЙ, БОЕВИК}