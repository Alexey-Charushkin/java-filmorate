package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmDaoStorage extends FilmStorage {

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    void addMPARating(Film film);
}
