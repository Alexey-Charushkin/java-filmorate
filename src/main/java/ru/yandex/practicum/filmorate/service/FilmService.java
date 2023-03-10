package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EmptyFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final Validate validator;

    private Long id = 0L;

    public Film create(Film film) {
        validator.validate(film);
        film.setId(++id);
        log.info("Фильм добавлен {}.", film);
        filmStorage.add(film);
        return film;
    }


    public ResponseEntity<?> update(Film film) {
        Optional<Film> oldItem = Optional.ofNullable(filmStorage.getFilm(film.getId()));
        if (!oldItem.isPresent()) {
            log.warn("Ошибка обновления id {} отсутствует в базе.", film.getId());
            throw new EmptyFilmException("Ошибка обновления фильм с id " + film.getId() + " отсутствует в базе.");
        }
        validator.validate(film);
        log.info("Фильм обновлён {}.", film);
        filmStorage.update(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", filmStorage.getFilms().size());
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film findById(Long id) {
        validator.filmIsPresent(id);
        log.info("Фильм с id {} найден.", id);
        return filmStorage.getFilm(id);
    }

    public Film addLike(Long filmId, Long userId) {
        User user = validator.userIsPresent(userId);
        Film film = validator.filmIsPresent(filmId);
        log.info("Пользователь {} добавил лайк фильму {}.", user.getName(), film.getName());
        film.setRate(film.getRate() + 1);
        film.setUserAddLikeFilm(userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        User user = validator.userIsPresent(userId);
        Film film = validator.filmIsPresent(filmId);
        log.info("Пользователь {} удалил лайк фильму {}.", user.getName(), film.getName());
        film.setRate(film.getRate() - 1);
        film.removeUserLikeFilm(userId);
        return film;
    }

    public List<Film> filmsPopular(Integer count) {
        return filmStorage.getFilms().values().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    Comparator<Film> comparator = (o1, o2) -> o2.getRate() - o1.getRate();
}
