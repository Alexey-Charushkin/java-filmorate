package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationDescriptionSizeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationDurationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@Service
public class Validate<T> {
    private boolean isValid = true;

    public void validate(T item) {

        if (item.getClass().equals(Film.class)) {
            Optional<String> filmDescription = Optional.ofNullable(((Film) item).getDescription());
            if (filmDescription.isPresent()) {
                if (filmDescription.get().length() > 200) {
                    log.warn("Длина описания фильма не может быть больше 200 символов.");
                    isValid = false;
                    throw new ValidationDescriptionSizeException();
                }
            }

            Optional<Integer> filmDuration = Optional.ofNullable(((Film) item).getDuration());
            if (filmDuration.isPresent()) {
                if (filmDuration.get() < 0) {
                    log.warn("Продолжительность фильма должна быть положительной.");
                    isValid = false;
                    throw new ValidationDurationException();
                }
            }

            Optional<LocalDate> releaseDate = Optional.ofNullable(((Film) item).getReleaseDate());
            if (releaseDate.isPresent()) {
                if (releaseDate.get().isBefore(LocalDate.of(1895, 12, 28))) {
                    log.warn("Дата релиза фильма раньше 28 декабря 1895 года.");
                    isValid = false;
                    throw new ValidationReleaseDateException();
                }
            }
        }

        if (item.getClass().equals(User.class)) {
            Optional<String> userName = Optional.ofNullable(((User) item).getName());
            if (!userName.isPresent() || ((User) item).getName().isEmpty()) {
                ((User) item).setName(((User) item).getLogin());
            }
        }
    }
}
