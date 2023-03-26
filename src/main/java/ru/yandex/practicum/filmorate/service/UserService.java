package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.compare;
import static java.lang.Long.max;

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
        userStorage.addFriend(id, idNewFriend);
    }

    public void deleteFriend(long id, long idDeleteFriend) {
        userStorage.getUsers().get(id).getFriends().remove(idDeleteFriend);
        userStorage.getUsers().get(idDeleteFriend).getFriends().remove(id);
    }

    public List<User> findAllGenerateFriends(long id, long otherId) {
        List<Long> generalFriends = getUserStorage().getUsers().get(id).getFriends().stream()
                .filter(getUserStorage().getUsers().get(otherId).getFriends()::contains)
                .sorted(Long::compareTo)
                .collect(Collectors.toList());

        return getUserStorage().getUsers().values().stream()
                .filter(x -> generalFriends.contains(x.getId()))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());

    }

    /*
     return filmStorage.getFilms().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    } */


    private int compare(User p0, User p1) {
        if (p0.getId() == p1.getId()) {
            return 0;
        }
        if (p0.getId() > p1.getId()) {
            return 1;
        }
        if (p0.getId() < p1.getId()) {
            return -1;
        }
        return -2;
    }


}
