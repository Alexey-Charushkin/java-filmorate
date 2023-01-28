package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j2
@RestController
public class UserController {

    private Integer userId = 1;
    Set<User> users = new HashSet<>();

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {

        Optional<String> userName = Optional.ofNullable(user.getName());
        if (userName.isPresent()) {
            if (user.getName().isEmpty()) user.setName(user.getLogin());
        } else user.setName(user.getLogin());

        log.info("Пользователь добавлен.");
        user.setId(userId);
        users.add(user);
        userId++;
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {

        Optional<User> oldUser = users.stream().filter(u -> u.getId().equals(user.getId())).findAny();

        if (oldUser.isPresent()) {
            log.info("Пользователь обновлён.");
            users.remove(oldUser.get());
            users.add(user);
            return user;

        } else {
            log.warn("Невозможно обновить пользователя, так ка его нет в базе.");
            throw new EmptyUserException();
        }
    }

    @GetMapping("/users")
    public Set<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

}
