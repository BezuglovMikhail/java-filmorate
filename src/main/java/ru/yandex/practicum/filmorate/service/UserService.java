package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void createFriend(long id, long idNewFriend) {
        userStorage.validateUser(userStorage.getUsers().get(id));
        userStorage.validateUser(userStorage.getUsers().get(idNewFriend));
        userStorage.getUsers().get(id).getFriends().add(idNewFriend);
        userStorage.getUsers().get(idNewFriend).getFriends().add(id);
    }

    public void deleteFriend(long id, long idDeleteFriend) {
        userStorage.getUsers().get(id).getFriends().remove(idDeleteFriend);
        userStorage.getUsers().get(idDeleteFriend).getFriends().remove(id);
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
