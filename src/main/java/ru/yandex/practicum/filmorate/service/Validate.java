package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@Service
public class Validate<T> {

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
                throw new ValidationReleaseDateException();
            }
        }
    }

    private void userValidate(User user) {
        Optional<String> userName = Optional.ofNullable(user.getName());
        if (!userName.isPresent() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
