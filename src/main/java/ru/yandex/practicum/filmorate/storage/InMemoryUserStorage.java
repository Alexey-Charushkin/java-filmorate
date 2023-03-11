package ru.yandex.practicum.filmorate.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Log4j2
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    @Override
    public User findUserById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) users;
    }

    @Override
    public void add(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void remove(Long id) {
        users.remove(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {

    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

    }
}
