package ru.yandex.practicum.filmorate.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Log4j2
@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Override
    public List<Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilm(Long id) {
      //  return films.get(id);
        return null;
    }

    @Override
    public void add(Film film) {
       // films.add(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        //films.put(film.getId(), film);
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void removeLike(Long filmId, Long userId) {

    }

}
