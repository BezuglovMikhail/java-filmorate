package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Validated
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public Optional<User> findById(@PathVariable long userId) {
        return userService.findByIdUser(userId);
    }

    @PostMapping
    public Optional<User> create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public Optional<User> update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriendsById(@PathVariable long id) {
        return userService.findFriendsByIdUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findGeneralFriends(@PathVariable(value = "id") Long id,
                                               @PathVariable(value = "otherId") Long otherId) {
        return userService.findAllGenerateFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable(value = "id") Long id, @PathVariable(value = "friendId") Long friendId) {
        log.debug("Пользователь с id = {} добавлен в друзья пользователю id = {}", friendId, id);
        userService.createFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void dellFriends(@PathVariable(value = "id") Long userId, @PathVariable(value = "friendId") Long friendId) {
        userService.deleteFriend(userId, friendId);
        log.debug("Пользователь с id = {} удален из друзей пользователя id = {}", friendId, userId);
    }
}
