package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
@Service
public class FilmService {
    FilmStorage filmStorage;
    FilmService(FilmStorage filmStorage) { this.filmStorage = filmStorage; }



}
