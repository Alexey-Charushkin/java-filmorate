package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class AbstractController<T extends Item> {

    private final Map<Integer, T> data = new HashMap<>();
    private Integer id = 0;

    public T create(@Valid @RequestBody T item) {
        validate(item);
        item.setId(++id);
        data.put(id, item);
        return item;
    }

    public T update(@Valid @RequestBody T item) {
        validate(item);
        item.setId(++id);
        data.put(id, item);
        return item;
    }

    public List<T> findAll() {
        List<T> allFilms = data.values().stream().collect(Collectors.toList());
        return allFilms;
   }
    abstract void validate(T item);
}
