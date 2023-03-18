package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.UserStorage.InMemoryUserStorage;

import java.util.Set;

@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    public long addFriend(long id, long idNewFriend) {
        inMemoryUserStorage.getUsers().get(id).getFriends().add(idNewFriend);
        inMemoryUserStorage.getUsers().get(idNewFriend).getFriends().add(id);

        return idNewFriend;
    }

    public void deleteFriend(long id, long idDeleteFriend) {
        inMemoryUserStorage.getUsers().get(id).getFriends().remove(idDeleteFriend);
        inMemoryUserStorage.getUsers().get(idDeleteFriend).getFriends().remove(id);
    }

    public Set<Long> printAllFriends(long id) {
        return inMemoryUserStorage.getUsers().get(id).getFriends();
    }

}
