package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/films")
public class FilmController {
    private InMemoryFilmStorage inMemoryFilmStorage;
      FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.info("Фильм добавлен {}.", film);
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        log.info("Фильм обновлён {}.", film);
        return inMemoryFilmStorage.update(film);
    }

    @GetMapping()
    public List<Film> findAll() {
        List<Film> films = inMemoryFilmStorage.findAll();
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }

}
