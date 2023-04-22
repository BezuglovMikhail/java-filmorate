package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
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

    public User addFriend(long id, long idNewFriend) {
        if (!users.containsKey(id) && id != 0) {
            throw new NotFoundException("Пользователя с id = " + id + " нет.");
        }
        if (!users.containsKey(idNewFriend) && idNewFriend != 0) {
            throw new NotFoundException("Пользователя с id = " + idNewFriend + " нет.");
        }
        users.get(id).getFriends().add(idNewFriend);
        users.get(idNewFriend).getFriends().add(id);
        return users.get(idNewFriend);
    }

    public void deleteFriend(long id, long idDeleteFriend) {
        users.get(id).getFriends().remove(idDeleteFriend);
        users.get(idDeleteFriend).getFriends().remove(id);
    }

    public Set<User> findFriendsByIdUser(long id) {
        return users.values().stream()
                .filter(x -> users.get(id).getFriends().contains(x.getId()))
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
