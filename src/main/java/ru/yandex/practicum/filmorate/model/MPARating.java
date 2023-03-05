package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
class MPARating {
    private String filmMPARating;
 //   enum filmMPARatings {G, PG, PG-13, R, NC-17}
}
