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
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return users.values().stream()
                .filter(x -> Objects.equals(x.getEmail(), user.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (validateUser(user.getId())) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            }
        }
        return users.values().stream()
                .filter(x -> Objects.equals(x.getEmail(), user.getEmail()))
                .findFirst();
    }

    @Override
    public boolean validateUser(Long userId) {
        if (!users.containsKey(userId) && userId != 0) {
            throw new UserNotFoundException("Пользователя с id = " + userId + " нет.");
        }
        return true;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден.", userId));
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public User removeUser(User user) {
        users.remove(user.getId());
        return user;
    }
}
