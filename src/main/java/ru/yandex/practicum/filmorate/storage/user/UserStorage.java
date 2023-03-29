package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Optional;

public interface UserStorage {
    long generateId();

    Optional<User> saveUser(User user);

    boolean validateUser(User user);

    HashMap<Long, User> getUsers();
}
