package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> films = new HashMap<>();

    Film getFilm(Long id);

    Map<Long, Film> getFilms();

    void add(Film film);

    void update(Film film);

}
