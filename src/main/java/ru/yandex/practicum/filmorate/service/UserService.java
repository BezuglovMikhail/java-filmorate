package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {
    @Autowired
    private UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public Optional<User> addUser(User user) {
        return getUserStorage().saveUser(user);
    }

    public User createFriend(long id, long idNewFriend) {
        getUserStorage().validateUser(getUserStorage().getUsers().get(id));
        getUserStorage().validateUser(getUserStorage().getUsers().get(idNewFriend));
        getUserStorage().getUsers().get(id).getFriends().add(idNewFriend);
        getUserStorage().getUsers().get(idNewFriend).getFriends().add(id);
        return getUserStorage().getUsers().get(idNewFriend);
    }

    public void deleteFriend(long id, long idDeleteFriend) {
        getUserStorage().getUsers().get(id).getFriends().remove(idDeleteFriend);
        getUserStorage().getUsers().get(idDeleteFriend).getFriends().remove(id);
    }

    public User findByIdUser(long userId) {
        if (!getUserStorage().getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден.", userId));
        }
        return getUserStorage().getUsers().get(userId);
    }

    public Collection<User> findAll() {
        return getUserStorage().getUsers().values();
    }

    public List<User> findFriendsByIdUser(@PathVariable long id) {
        return getUserStorage().getUsers().values().stream()
                .filter(x -> getUserStorage().getUsers().get(id).getFriends().contains(x.getId()))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    public List<User> findAllGenerateFriends(long id, long otherId) {
        List<Long> generalFriends = getUserStorage().getUsers().get(id).getFriends().stream()
                .filter(getUserStorage().getUsers().get(otherId).getFriends()::contains)
                .collect(Collectors.toList());

        return getUserStorage().getUsers().values().stream()
                .filter(x -> generalFriends.contains(x.getId()))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }
}
