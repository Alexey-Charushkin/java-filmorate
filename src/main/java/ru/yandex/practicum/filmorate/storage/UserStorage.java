package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> users = new HashMap<>();

    User findUserById(Long id);

    Map<Long, User> getUsers();

    void add(User user);

    void update(User user);

    void remove(User user);
}
