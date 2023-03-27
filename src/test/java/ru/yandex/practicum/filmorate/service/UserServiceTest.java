package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);

    @BeforeEach
    void createUsersForTest() throws IOException, InterruptedException {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        User user2 = new User();
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(1988, 1, 16));

        User user3 = new User();
        user2.setEmail("mailrtyj@mail.ru");
        user2.setName("Nikyfk");
        user2.setLogin("Login678");
        user2.setBirthday(LocalDate.of(2005, 5, 26));

        User user4 = new User();
        user2.setEmail("mailrtweryyj@mail.ru");
        user2.setName("Nikyfkeryh");
        user2.setLogin("Login678678");
        user2.setBirthday(LocalDate.of(2001, 10, 30));

        userStorage.saveUser(user);
        userStorage.saveUser(user2);
        userStorage.saveUser(user3);
        userStorage.saveUser(user4);
    }

    @Test
    void createFriendTest() {
        userService.createFriend(1, 2);
        userService.createFriend(1, 3);

        assertEquals(Set.of(2L, 3L), userStorage.getUsers().get(1L).getFriends());
        assertEquals(Set.of(1L), userStorage.getUsers().get(2L).getFriends());
    }

    @Test
    void deleteFriendTest() {
        userService.createFriend(1, 2);
        userService.createFriend(1, 3);
        userService.deleteFriend(1, 2);
        userService.deleteFriend(2, 1);

        assertEquals(Set.of(3L), userStorage.getUsers().get(1L).getFriends());
        assertEquals(Set.of(), userStorage.getUsers().get(2L).getFriends());
    }

    @Test
    void findAllGenerateFriends() {
        userService.createFriend(1, 2);
        userService.createFriend(1, 3);
        userService.createFriend(1, 4);
        userService.createFriend(4, 3);
        userService.createFriend(4, 2);

        assertEquals(List.of(userStorage.getUsers().get(2L), userStorage.getUsers().get(3L)),
                userService.findAllGenerateFriends(1, 4));
        assertEquals(List.of(userStorage.getUsers().get(2L), userStorage.getUsers().get(3L)),
                userService.findAllGenerateFriends(4, 1));
    }
}
