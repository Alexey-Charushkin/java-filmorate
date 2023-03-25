package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@Service
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Validate<T> {

    UserStorage userStorage;
    FilmStorage filmStorage;

    public void validate(T item) {
        if (item.getClass().equals(Film.class)) {
            filmValidate((Film) item);
        } else if (item.getClass().equals(User.class)) {
            userValidate((User) item);
        }
    }

    private void filmValidate(Film film) {

        Optional<LocalDate> releaseDate = Optional.ofNullable(film.getReleaseDate());
        if (releaseDate.isPresent()) {
            if (releaseDate.get().isBefore(LocalDate.of(1895, 12, 28))) {
                log.warn("Дата релиза фильма раньше 28 декабря 1895 года.");
                throw new ValidationException("Дата релиза фильма раньше 28 декабря 1895 года.");
            }
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания фильма не может быть больше 200 символов.");
            throw new ValidationException("Длина описания фильма не может быть больше 200 символов.");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма не может быть отрицательной.");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
    }

    private void userValidate(User user) {
        for (int i = 0; i < (user.getLogin()).trim().length(); i++) {
            if ((user.getLogin()).trim().charAt(i) == ' ') {
                log.warn("Логин не должен содержать пробелов.");
                throw new ValidationException("Логин не должен содержать пробелов.");
            }
        }

        Optional<String> userName = Optional.ofNullable(user.getName());
        if (!userName.isPresent() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    User userIsPresent(Long id) {
        Optional<User> isUser = Optional.ofNullable(userStorage.findUserById(id));
        if (!isUser.isPresent()) {
            log.warn("Пользователь c id {} отсутствует в базе.", id);
            try {
                throw new FilmNotFoundException("Пользователь с id %s отсутствует в базе.", id);
            } catch (FilmNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return isUser.get();
    }

    Film filmIsPresent(Long id) {
        Optional<Film> isFilm = Optional.ofNullable(filmStorage.getFilm(id));
        if (!isFilm.isPresent()) {
            log.warn("Фильм c id {} отсутствует в базе.", id);
            try {
                throw new FilmNotFoundException("Фильм с id %s отсутствует в базе.", id);
            } catch (FilmNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return isFilm.get();
    }
}
