package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
public class UserController {
    UserRepository userRepository = new UserRepository();

    @GetMapping(value = "/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userRepository.getUsers().size());
        return userRepository.getUsers().values();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        userRepository.saveUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User appDate(@Valid @RequestBody User user) {
        userRepository.saveUser(user);
        return user;
    }
}