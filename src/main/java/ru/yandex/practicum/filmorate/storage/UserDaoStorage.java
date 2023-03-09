package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDaoStorage extends UserStorage {
    void addFriend(Long userId, Long friendId);

    @Override
    List<User> getUsers();

    @Override
    void add(User user);

    @Override
    void update(User user);

    @Override
    void remove(Long id);
}
