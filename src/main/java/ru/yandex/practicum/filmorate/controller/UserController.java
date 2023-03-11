package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserRepository userRepository = new UserRepository();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userRepository.getUsers().size());
        return userRepository.getUsers().values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userRepository.saveUser(user);
        log.debug("Добавлен пользователь с id = {}", user.getId());
        return user;
    }

    @PutMapping
    public User appDate(@Valid @RequestBody User user) {
        userRepository.saveUser(user);
        log.debug("Обновлен пользователь с id = {}", user.getId());
        return user;
    }
}