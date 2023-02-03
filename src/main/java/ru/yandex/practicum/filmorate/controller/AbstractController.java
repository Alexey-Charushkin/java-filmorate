package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.Item;
import lombok.extern.log4j.Log4j2;

import javax.validation.Valid;
import java.util.*;

@Getter
@Setter
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractController<T extends Item> {
    private final Map<Long, T> data = new HashMap<>();
    private Long id = 0L;

    public T create(@Valid @RequestBody T item) {
        validate(item);
        item.setId(++id);
        data.put(id, item);
        return item;
    }

    public ResponseEntity<?> update(@Valid @RequestBody T item) {
        Optional<T> oldItem = Optional.ofNullable(data.get(item.getId()));
        if (!oldItem.isPresent()) {
            log.warn("Ошибка обновления id {} отсутствует в базе.", item.getId());
            throw new EmptyUserException();
        }
        validate(item);
        data.put(id, item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    abstract void validate(T item);
}
