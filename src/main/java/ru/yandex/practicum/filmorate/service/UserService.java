package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public Optional<User> addUser(User user) {
        return userStorage.saveUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void createFriend(long userId, long idNewFriend) {
        userStorage.addFriend(userId, idNewFriend);
    }

    public void deleteFriend(long userId, long idDeleteFriend) {
        userStorage.removeFriend(userId, idDeleteFriend);
    }

    public Optional<User> findByIdUser(long userId) {
        return userStorage.findUserById(userId);
    }

    public Collection<User> findAll() {
        return userStorage.getUsers();
    }

    public Collection<User> findFriendsByIdUser(@PathVariable long id) {
        return userStorage.getFriends(id);
    }

    public Collection<User> findAllGenerateFriends(long id, long otherId) {
        return userStorage.getFriends(id).stream()
                .filter(x -> userStorage.getFriends(otherId).contains(x))
                .collect(Collectors.toList());
    }
}
