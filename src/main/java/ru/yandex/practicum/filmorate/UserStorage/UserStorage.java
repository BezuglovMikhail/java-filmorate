package ru.yandex.practicum.filmorate.UserStorage;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;


public interface UserStorage {
    long generateId();

    void saveUser(User user);

    boolean validateUser(User user);

    HashMap<Long, User> getUsers();

    public long getIdUser();
}
