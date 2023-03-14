package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {

    List<Film> films = new ArrayList<>();

    Film getFilm(Long id);

    List<Film> getFilms();

    void add(Film film);

    void update(Film film);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Genre> getAllGenres();

    Genre getGenreById(Long id);

    List<MPA> getAllMPA();

    MPA getMPAById(Long id);
}

