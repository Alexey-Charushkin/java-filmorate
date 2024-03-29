package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("FilmDbStorage")
    FilmStorage filmStorage;
    Validate validator;

    public Film create(Film film) {
        validator.validate(film);
        filmStorage.add(film);
        return film;
    }


    public ResponseEntity<?> update(Film film) {
        Optional<Film> oldItem = Optional.ofNullable(filmStorage.getFilm(film.getId()));
        validator.validate(film);

        if (oldItem.isPresent()) {
            filmStorage.update(film);
        }
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    public Film findById(Long id) {
        validator.filmIsPresent(id);
        return filmStorage.getFilm(id);
    }

    public Film addLike(Long filmId, Long userId) {
        User user = validator.userIsPresent(userId);
        Film film = validator.filmIsPresent(filmId);
        log.info("Пользователь {} добавил лайк фильму {}.", user.getName(), film.getName());
        film.setRate(film.getRate() + 1);
        film.setUserAddLikeFilm(userId);
        filmStorage.addLike(filmId, userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        User user = validator.userIsPresent(userId);
        Film film = validator.filmIsPresent(filmId);
        log.info("Пользователь {} удалил лайк фильму {}.", user.getName(), film.getName());
        film.setRate(film.getRate() - 1);
        film.removeUserLikeFilm(userId);
        filmStorage.removeLike(filmId, userId);
        return film;
    }

    public List<Film> filmsPopular(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    Comparator<Film> comparator = (o1, o2) -> o2.getRate() - o1.getRate();

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }


    public Genre getGenreById(Long id) {
        return filmStorage.getGenreById(id);
    }

    public List<MPA> getAllMPA() {
        return filmStorage.getAllMPA();
    }

    public MPA getMPAById(Long id) {
        return filmStorage.getMPAById(id);
    }
}
