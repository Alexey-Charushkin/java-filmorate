package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

class FilmDbStorage implements FilmStorage {
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
}