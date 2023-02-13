package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(filmService.findAll());
    }

}
