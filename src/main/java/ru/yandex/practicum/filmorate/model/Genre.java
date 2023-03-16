package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
@RequiredArgsConstructor
public
class Genre {
    int id;
    String name;
}

