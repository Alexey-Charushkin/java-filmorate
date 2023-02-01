package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.*;

@Log4j2
@RestController
public class UserController extends AbstractController<User> {

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Пользователь добавлен {}.", user);
        return super.create(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        List<User> users = super.findAll();
        Optional<User> oldUser = users.stream().filter(u -> u.getId().equals(user.getId())).findAny();
        if (oldUser.isPresent()) {
            log.info("Пользователь обновлён {}.", user);
            return super.update(user);
        } else {
            log.warn("Невозможно обновить пользователя, так ка его нет в базе.");
            throw new EmptyUserException();
        }
    }

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> users = super.findAll();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public void validate(User item) {
        Optional<String> userName = Optional.ofNullable(item.getName());
        if (!userName.isPresent() || item.getName().isEmpty()) {
            item.setName(item.getLogin());
        }
    }
}
