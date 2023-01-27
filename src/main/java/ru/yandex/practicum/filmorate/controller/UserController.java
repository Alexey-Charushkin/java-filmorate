package ru.yandex.practicum.filmorate.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


@Log4j2
@RestController
public class UserController {
    Set<User> users = new HashSet<>();

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Пользователь добавлен.");
        users.add(user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        //       log.info("Пользователь обновлён.");
        users.add(user);
        return user;
    }

    @GetMapping("/users")
    public Set<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @GetMapping("/home")
    public String homePage() {
        return "Filmorate";
    }
}
//@RestController
//public class PostController {
//    private static final Logger log = LoggerFactory.getLogger(PostController.class);
//    private final List<Post> posts = new ArrayList<>();
//
//    @GetMapping("/posts")
//    public List<Post> findAll() {
//        log.debug("Текущее количество постов: {}", posts.size());
//        return posts;
//    }
//
//    @PostMapping(value = "/post")
//    public void create(@RequestBody Post post) {
//        posts.add(post);
//    }
//}