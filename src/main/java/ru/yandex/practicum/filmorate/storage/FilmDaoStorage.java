package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

interface FilmDaoStorage extends FilmStorage {

    void addMPARating(Film film);
}
