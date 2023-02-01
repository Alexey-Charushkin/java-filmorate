package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Item;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        item.setId(id);
        data.put(id, item);
        return item;
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
   }
    abstract void validate(T item);
}
