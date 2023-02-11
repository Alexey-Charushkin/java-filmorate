package ru.yandex.practicum.filmorate.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.*;

@Log4j2
@Component
public class InMemoryUserStorage implements UserStorage {

    private Validate validator;

    InMemoryUserStorage(Validate validator) {
        this.validator = validator;
    }

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User create(User user) {
        validator.validate(user);
        user.setId(++id);
        users.put(id, user);
        return user;
    }

    @Override
    public ResponseEntity<?> update(User user) {
        Optional<User> oldItem = Optional.ofNullable(users.get(user.getId()));
        if (!oldItem.isPresent()) {
            log.warn("Ошибка обновления id {} отсутствует в базе.", user.getId());
            throw new EmptyUserException("Ошибка обновления. Пользователь с id " + user.getId()
                    + " отсутствует в базе.");
        }
        validator.validate(user);
        users.put(id, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public User remove() {
        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

}
