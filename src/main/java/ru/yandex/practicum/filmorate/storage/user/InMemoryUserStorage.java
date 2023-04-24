package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Data
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
            throw new NotFoundException("Пользователя с id = " + userId + " нет");
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
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public User removeUser(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (!users.containsKey(userId) && userId != 0) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
        if (!users.containsKey(friendId) && friendId != 0) {
            throw new NotFoundException("Пользователя с id = " + friendId + " нет.");
        }
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return users.values().stream()
                .filter(x -> users.get(userId).getFriends().contains(x.getId()))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toSet());
    }

    public Set<User> findAllGenerateFriends(long id, long otherId) {
        Set<Long> generalFriends = users.get(id).getFriends().stream()
                .filter(users.get(otherId).getFriends()::contains)
                .collect(Collectors.toSet());

        return users.values().stream()
                .filter(x -> generalFriends.contains(x.getId()))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toSet());
    }
}
