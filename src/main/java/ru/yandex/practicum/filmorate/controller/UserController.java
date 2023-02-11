package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private InMemoryUserStorage inMemoryUserStorage;

    UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info("Пользователь добавлен {}.", user);
        return inMemoryUserStorage.create(user);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        log.info("Пользователь обновлён {}.", user);
        return inMemoryUserStorage.update(user);

    }

    @GetMapping()
    public List<User> findAll() {
        List<User> users = inMemoryUserStorage.findAll();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }
}
