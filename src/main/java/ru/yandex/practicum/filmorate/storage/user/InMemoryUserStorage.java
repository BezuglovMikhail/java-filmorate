package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private long idUser = 0;

    @Override
    public long generateId() {
        return ++idUser;
    }

    @Override
    public Optional<User> saveUser(User user) {
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
        return users.values().stream()
                .filter(x -> Objects.equals(x.getEmail(), user.getEmail()))
                .findFirst();
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
}
