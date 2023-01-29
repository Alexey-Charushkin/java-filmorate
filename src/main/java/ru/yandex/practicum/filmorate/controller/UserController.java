package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class UserController {

    private Integer userId = 1;
    private Set<User> users = new HashSet<>();

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {

        Optional<String> userName = Optional.ofNullable(user.getName());
        if (!userName.isPresent() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

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
    public List<User> findAll() {
        List allUsers = users.stream().collect(Collectors.toList());
        log.info("Текущее количество пользователей: {}", allUsers.size());
        return allUsers;
    }

}
