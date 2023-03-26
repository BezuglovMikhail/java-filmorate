package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;


public interface UserStorage {
    long generateId();

    void saveUser(User user);

    boolean validateUser(User user);
    HashMap<Long, User> getUsers();
    Long getUserId();

    void addFriend(long id, long idNewFriend);
}
