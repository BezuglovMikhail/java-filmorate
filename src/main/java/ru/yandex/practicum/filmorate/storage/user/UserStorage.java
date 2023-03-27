package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public interface UserStorage {
    long generateId();

    void saveUser(User user);

    boolean validateUser(User user);

    HashMap<Long, User> getUsers();
}
