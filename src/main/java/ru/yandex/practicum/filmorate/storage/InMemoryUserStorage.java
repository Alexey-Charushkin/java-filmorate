package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class InMemoryUserStorage implements UserStorage {

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(User user) {
        return null;
    }

    @Override
    public User remove() {
        return null;
    }
}
