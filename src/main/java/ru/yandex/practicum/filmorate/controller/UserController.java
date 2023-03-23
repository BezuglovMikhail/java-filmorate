package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return userService.getUserStorage().getUsers().values().stream()
                .filter(x -> x.getId() == userId)
                .findFirst();
    }

    @GetMapping("/{id}")
    public Set<Long> findFriendsById(@PathVariable long id) {
        return userService.getUserStorage().getUsers().values().stream()
                .filter(x -> x.getId() == id)
                .findFirst().get().getFriends();
    }


    @PutMapping ("/{id}/friends/{friendId}")
    public User addFriends(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "friendId") Long friendId
    ) {
        userService.addFriend(id, friendId);
        log.debug("Пользователь с id = {friendId} добавлен в друзья пользователю id = {id}");
        return userService.getUserStorage().getUsers().get(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User dellFriends(
            @RequestParam(value = "id") Long userId,
            @RequestParam(value = "friendId") Long friendId
    ) {
        userService.deleteFriend(userId, friendId);
        log.debug("Пользователь с id = {friendId} удален из друзей пользователя id = {id}");
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