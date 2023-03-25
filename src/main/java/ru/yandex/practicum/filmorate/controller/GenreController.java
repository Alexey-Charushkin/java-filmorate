package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/genres")
class GenreController {
    private final FilmService filmService;

    GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Genre> findAllGenres() {
        log.info("Текущее количество жанров фильмов: {}", filmService.getAllGenres().size());
        return new ArrayList<>(filmService.getAllGenres());
    }

    @GetMapping("{id}")
    public Genre findById(@PathVariable Long id) {
        log.info("Жанр фильма с id {}: {}", id, filmService.getGenreById(id));
        return filmService.getGenreById(id);
    }
}
