package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> users = new HashMap<>();

    public User findUserById(Long id);

    public Map<Long, User> getUsers();

    public void add(User user);

    public void update(User user);

    public void remove(User user);
}
