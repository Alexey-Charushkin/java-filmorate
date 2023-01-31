package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EmptyFilmException;

import ru.yandex.practicum.filmorate.exceptions.ValidationDescriptionSizeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationDurationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class FilmController {
    public static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
        }
    }
    private Integer filmId = 1;

    private Set<Film> films = new HashSet<>();

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {

        boolean isValid = filmValidate(film);
        if (isValid) {
            log.info("Фильм добавлен.");
            film.setId(filmId);
            films.add(film);
            filmId++;
        }
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {

        Optional<Film> oldFilm = films.stream().filter(u -> u.getId().equals(film.getId())).findAny();
        if (oldFilm.isPresent()) {
            log.info("Фильм обновлён.");
            films.remove(oldFilm.get());
            films.add(film);
            return film;

        } else {
            log.warn("Невозможно обновить фильм, так ка его нет в базе.");
            throw new EmptyFilmException();
        }
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        List allFilms = films.stream().collect(Collectors.toList());
        log.info("Текущее количество фильмов: {}", allFilms.size());
        return allFilms;
    }

    public boolean filmValidate(Film film) {

        boolean isValid = true;

        Optional<String> filmDescription = Optional.ofNullable(film.getDescription());
        if (filmDescription.isPresent()) {
            if (filmDescription.get().length() > 200) {
                isValid = false;
                log.warn("Длина описания фильма не может быть больше 200 символов.");
                throw new ValidationDescriptionSizeException();
            }
        }

        Optional<Duration> filmDuration = Optional.ofNullable(film.getDuration());
        if (filmDuration.isPresent()) {
            if (filmDuration.get().toMillis() < 0) {
                isValid = false;
                log.warn("Продолжительность фильма должна быть положительной.");
                throw new ValidationDurationException();
            }
        }

        Optional<LocalDate> releaseDate = Optional.ofNullable(film.getReleaseDate());
        if (releaseDate.isPresent()) {
            if (releaseDate.get().isBefore(LocalDate.of(1895, 12, 28))) {
                isValid = false;
                log.warn("Дата релиза фильма раньше 28 декабря 1895 года.");
                throw new ValidationReleaseDateException();
            }
        }
        return isValid;
    }
    
}
