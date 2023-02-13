package ru.yandex.practicum.filmorate.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EmptyFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.*;

@Log4j2
@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public void add(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Film remove() {
        return null;
    }


}
