package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationDescriptionSizeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationDurationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
public class FilmController extends AbstractController<Film> {

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
            log.info("Фильм добавлен {}.", film);
            return super.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {

//        Optional<Film> oldFilm = films.stream().filter(u -> u.getId().equals(film.getId())).findAny();
//        if (oldFilm.isPresent()) {
            log.info("Фильм обновлён {}.", film);
            return super.update(film);

//        } else {
//            log.warn("Невозможно обновить фильм, так ка его нет в базе.");
//            throw new EmptyFilmException();
//        }
    }

   @GetMapping("/films")
    public List findAll() {
       List allData = super.findAll();
       List<Object> films = new ArrayList<>();
       Integer index = 0;
       for (Object data : allData) {
           if (data.getClass().equals(Film.class)) films.add(index++, data);
       }
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }

    public void validate(Film film) {

        Optional<String> filmDescription = Optional.ofNullable(film.getDescription());
        if (filmDescription.isPresent()) {
            if (filmDescription.get().length() > 200) {
               log.warn("Длина описания фильма не может быть больше 200 символов.");
                throw new ValidationDescriptionSizeException();
            }
        }

        Optional<Integer> filmDuration = Optional.ofNullable(film.getDuration());
        if (filmDuration.isPresent()) {
            if (filmDuration.get() < 0) {
                log.warn("Продолжительность фильма должна быть положительной.");
                throw new ValidationDurationException();
            }
        }

        Optional<LocalDate> releaseDate = Optional.ofNullable(film.getReleaseDate());
        if (releaseDate.isPresent()) {
            if (releaseDate.get().isBefore(LocalDate.of(1895, 12, 28))) {
                log.warn("Дата релиза фильма раньше 28 декабря 1895 года.");
                throw new ValidationReleaseDateException();
            }
        }
    }
}
