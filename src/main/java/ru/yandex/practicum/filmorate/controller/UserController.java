package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info("Пользователь добавлен {}.", user);
        return super.create(user);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        try {
            log.info("Пользователь обновлён {}.", user);
            return super.update(user);
        } catch (Exception exc) {
            log.info("Не обновлено! Пользователь {} отсутствует в базе.", user.getName());
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
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
