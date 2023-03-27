package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userService.getUserStorage().getUsers().size());
        return userService.getUserStorage().getUsers().values();
    }

    @GetMapping("/{userId}")
    public Optional<User> findById(@PathVariable long userId) {
        if (!userService.getUserStorage().getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        return userService.getUserStorage().getUsers().values().stream()
                .filter(x -> x.getId() == userId)
                .findFirst();
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsById(@PathVariable long id) {
        return userService.getUserStorage().getUsers().values().stream()
                .filter(x -> userService.getUserStorage().getUsers().get(id).getFriends().contains(x.getId()))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findGeneralFriends(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "otherId") Long otherId
    ) {
        return userService.findAllGenerateFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "friendId") Long friendId
    ) {
        userService.createFriend(id, friendId);
        log.debug("Пользователь с id = {} добавлен в друзья пользователю id = {}", friendId, id);
        return userService.getUserStorage().getUsers().get(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User dellFriends(
            @PathVariable(value = "id") Long userId,
            @PathVariable(value = "friendId") Long friendId
    ) {
        userService.deleteFriend(userId, friendId);
        log.debug("Пользователь с id = {} удален из друзей пользователя id = {}", friendId, userId);
        return userService.getUserStorage().getUsers().get(friendId);
    }


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userService.getUserStorage().saveUser(user);
        log.debug("Добавлен пользователь с id = {}", user.getId());
        return userService.getUserStorage().getUsers().get(user.getId());
    }

    @PutMapping
    public User appDate(@Valid @RequestBody User user) {
        userService.getUserStorage().saveUser(user);
        log.debug("Обновлен пользователь с id = {}", user.getId());
        return userService.getUserStorage().getUsers().get(user.getId());
    }
}