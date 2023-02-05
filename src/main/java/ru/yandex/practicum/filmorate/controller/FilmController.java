package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.info("Фильм добавлен {}.", film);
        return super.create(film);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        try {
            log.info("Фильм обновлён {}.", film);
            return super.update(film);
        } catch (Exception exc) {
            log.info("Не обновлено! Фильм {} отсутствует в базе.", film.getName());
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public List<Film> findAll() {
        List<Film> films = super.findAll();
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }
}
