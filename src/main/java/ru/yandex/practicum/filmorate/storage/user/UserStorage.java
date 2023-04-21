package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    long generateId();

    Optional<User> saveUser(User user);

    Optional<User> updateUser(User user);

    boolean validateUser(Long userId);

    Collection<User> getUsers();

    Optional<User> findUserById(Long useId);

    User removeUser(User user);
}
