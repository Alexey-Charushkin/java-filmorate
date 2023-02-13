package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Log4j2
@Service
public class UserService {

    private Validate validator;
    private UserStorage userStorage;

    UserService(Validate validator, UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    private Long id = 0L;

    public User create(User user) {
        validator.validate(user);
        user.setId(++id);
        log.info("Пользователь добавлен {}.", user);
        userStorage.add(user);
        return user;
    }

    public ResponseEntity<?> update(User user) {
        Optional<User> oldItem = Optional.ofNullable(userStorage.getUser(user.getId()));
        if (!oldItem.isPresent()) {
            log.warn("Ошибка обновления id {} отсутствует в базе.", user.getId());
            throw new EmptyUserException("Ошибка обновления. Пользователь с id " + user.getId()
                    + " отсутствует в базе.");
        }
        validator.validate(user);
        log.info("Пользователь обновлён {}.", user);
        userStorage.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public User remove() {
        return null;
    }

    public List<User> findAll() {
        log.info("Текущее количество пользователей: {}", userStorage.getUsers().size());
        return new ArrayList<>(userStorage.getUsers().values());
    }
}