package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> users = new HashMap<>();

    User findUserById(Long id);

    List<User> getUsers();

    void add(User user);

    void update(User user);

    void remove(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<Long> findUserFriendsById(Long idUser);

    boolean isFriendExist(Long userId, Long friendId);
}
