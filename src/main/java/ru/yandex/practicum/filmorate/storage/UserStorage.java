package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    public User create(@Valid @RequestBody User user);

    public ResponseEntity<?> update(@Valid @RequestBody User user);

    public User remove();
    public List<User> findAll();
}
