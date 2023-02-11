package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {
    public Film create(@Valid @RequestBody Film film);
    public ResponseEntity<?> update(@Valid @RequestBody Film film);
    public Film remove();

}
