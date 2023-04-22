package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.friends.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {
    private UserStorage userStorage;
    private FriendStorage friendStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage, FriendStorage friendStorage) {
        this.userStorage = userDbStorage;
        this.friendStorage = friendStorage;
    }

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public Optional<User> addUser(User user) {
        return getUserStorage().saveUser(user);
    }

    public Optional<User> updateUser(User user) {
            return getUserStorage().updateUser(user);
    }

    public void createFriend(long userId, long idNewFriend) {
        getFriendStorage().addFriend(userId, idNewFriend);
    }

    public void deleteFriend(long userId, long idDeleteFriend) {
        getFriendStorage().removeFriend(userId, idDeleteFriend);
    }

    public Optional<User> findByIdUser(long userId) {
        return getUserStorage().findUserById(userId);
    }

    public Collection<User> findAll() {
        return getUserStorage().getUsers();
    }

    public Collection<User> findFriendsByIdUser(@PathVariable long id) {
        return friendStorage.getFriends(id);
    }

    public Collection<User> findAllGenerateFriends(long id, long otherId) {
        return friendStorage.getFriends(id).stream()
                .filter(x -> friendStorage.getFriends(otherId).contains(x))
                .collect(Collectors.toList());
    }
}
