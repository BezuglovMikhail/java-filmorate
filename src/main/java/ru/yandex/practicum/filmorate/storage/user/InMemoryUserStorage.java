package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    //private final HashMap<Long, User> users = new HashMap<>();

    private final HashMap<Long, User> users = new HashMap<>();
    private long idUser = 0;

    @Override
    public long generateId() {
        return ++idUser;
    }

    @Override
    public void saveUser(User user) {
        if (validateUser(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            } else {
                if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                user.setId(generateId());
                users.put(user.getId(), user);
            }
        }
    }
@Override
    public boolean validateUser(User user) {
        if (!users.containsKey(user.getId()) && user.getId() != 0) {
            throw new UserNotFoundException("Пользователя с id = " + user.getId() + " нет.");
        }
        return true;
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }

    @Override
    public Long getUserId() {
        return idUser;
    }

    @Override
    public void addFriend(long id, long idNewFriend) {
        if (getUsers().containsKey(id) && getUsers().containsKey(idNewFriend)) {
            getUsers().get(id).getFriends().add(idNewFriend);
            getUsers().get(idNewFriend).getFriends().add(id);
        }
        else {
            throw new UserNotFoundException("Пользователя с id = " + id + " или " + idNewFriend + " нет.");
        }
    }
}
