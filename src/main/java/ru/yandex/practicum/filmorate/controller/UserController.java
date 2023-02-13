package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(userService.findAll());
    }

    @GetMapping("{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }
    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, Long friendId) {
        return userService.addFriend(id, friendId);
    }
}
