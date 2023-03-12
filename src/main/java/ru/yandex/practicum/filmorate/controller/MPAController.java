package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/mpa")
public class MPAController {
    private final FilmService filmService;

    MPAController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<MPA> findAllMPAs() {
        // log.info("Текущее количество жанров фильмов: {}", filmService.getAllGenres.size());
        return new ArrayList<>(filmService.getAllMPA());
    }

    @GetMapping("{id}")
    public MPA findMPAById(@PathVariable Long id) {
        // log.info("Текущее количество жанров фильмов: {}", filmService.getAllGenres.size());
        return filmService.getMPAById(id);
    }

}


