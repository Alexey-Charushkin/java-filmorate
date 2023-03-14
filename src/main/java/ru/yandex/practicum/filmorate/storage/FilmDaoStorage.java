package ru.yandex.practicum.filmorate.storage;

public interface FilmDaoStorage extends FilmStorage {

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
