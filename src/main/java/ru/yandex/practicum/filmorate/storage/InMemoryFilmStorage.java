package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(Film film) {
        return null;
    }

    @Override
    public Film remove() {
        return null;
    }
}
