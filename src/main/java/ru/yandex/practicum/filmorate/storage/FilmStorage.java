package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> films = new HashMap<>();

    public Film getFilm(Long id);

    public Map<Long, Film> getFilms();

    public void add(Film film);

    public void update(Film film);

    public Film remove();

}
