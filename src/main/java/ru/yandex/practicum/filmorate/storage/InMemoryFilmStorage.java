package ru.yandex.practicum.filmorate.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.*;

@Log4j2
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Validate validator;
    public InMemoryFilmStorage(Validate validator) {
        this.validator = validator;
    }

    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @Override
    public Film create(Film film) {
        validator.validate(film);
        film.setId(++id);
        films.put(id, film);
        return film;
    }

    @Override
    public ResponseEntity<?> update(Film film) {
        Optional<Film> oldItem = Optional.ofNullable(films.get(film.getId()));
        if (!oldItem.isPresent()) {
            log.warn("Ошибка обновления id {} отсутствует в базе.", film.getId());
            throw new EmptyUserException();
        }
        validator.validate(film);
        films.put(id, film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @Override
    public Film remove() {
        return null;
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }
}
