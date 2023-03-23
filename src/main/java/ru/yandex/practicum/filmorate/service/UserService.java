package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

@Service
@Data
public class UserService {
    @Autowired
    private UserStorage userStorage;

    @Autowired
     public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    //InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    public long addFriend(long id, long idNewFriend) {
        userStorage.getUsers().get(id).getFriends().add(idNewFriend);
        userStorage.getUsers().get(idNewFriend).getFriends().add(id);

        return idNewFriend;
    }

    public void deleteFriend(long id, long idDeleteFriend) {
        userStorage.getUsers().get(id).getFriends().remove(idDeleteFriend);
        userStorage.getUsers().get(idDeleteFriend).getFriends().remove(id);
    }

    public Set<Long> printAllFriends(long id) {
        return userStorage.getUsers().get(id).getFriends();
    }

}
