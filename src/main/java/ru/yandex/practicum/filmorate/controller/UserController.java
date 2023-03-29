package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userService.findAll().size());
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public Optional<User> findById(@PathVariable long userId) {
        return userService.findByIdUser(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsById(@PathVariable long id) {
        return userService.findFriendsByIdUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findGeneralFriends(@PathVariable(value = "id") Long id,
                                         @PathVariable(value = "otherId") Long otherId) {
        return userService.findAllGenerateFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(@PathVariable(value = "id") Long id, @PathVariable(value = "friendId") Long friendId) {
        log.debug("Пользователь с id = {} добавлен в друзья пользователю id = {}", friendId, id);
        return userService.createFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void dellFriends(@PathVariable(value = "id") Long userId, @PathVariable(value = "friendId") Long friendId) {
        userService.deleteFriend(userId, friendId);
        log.debug("Пользователь с id = {} удален из друзей пользователя id = {}", friendId, userId);
    }

    @PostMapping
    public Optional<User> create(@Valid @RequestBody User user) {
        log.debug("Добавлен пользователь с id = {}", user.getId());
        return userService.addUser(user);
    }

    @PutMapping
    public Optional<User> appDate(@Valid @RequestBody User user) {
        log.debug("Обновлен пользователь с id = {}", user.getId());
        return userService.addUser(user);
    }
}
