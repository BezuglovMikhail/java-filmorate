package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceInMemoryTest {
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
        user3.setEmail("mailrtyj@mail.ru");
        user3.setName("Nikyfk");
        user3.setLogin("Login678");
        user3.setBirthday(LocalDate.of(2005, 5, 26));

        User user4 = new User();
        user4.setEmail("mailrtweryyj@mail.ru");
        user4.setName("Nikyfkeryh");
        user4.setLogin("Login678678");
        user4.setBirthday(LocalDate.of(2001, 10, 30));

        userStorage.saveUser(user);
        userStorage.saveUser(user2);
        userStorage.saveUser(user3);
        userStorage.saveUser(user4);
    }

    @Test
    void addUserTest() {
        User user5 = new User();
        user5.setEmail("mailTest@mail.ru");
        user5.setName("nameForTest");
        user5.setLogin("LoginForTest");
        user5.setBirthday(LocalDate.of(2011, 7, 20));

        userService.addUser(user5);

        assertEquals(Optional.of(user5), userStorage.findUserById (5L));
    }

    @Test
    void addUserAndAppDateTest() {
        User user5 = new User();
        user5.setEmail("mailTest@mail.ru");
        user5.setName("nameForTest");
        user5.setLogin("LoginForTest");
        user5.setBirthday(LocalDate.of(2011, 7, 20));

        User user5AppDate = new User();
        user5AppDate.setEmail("mailTestAppDate@mail.ru");
        user5AppDate.setName("nameForTestAppDate");
        user5AppDate.setLogin("LoginForTestAppDate");
        user5AppDate.setBirthday(LocalDate.of(2011, 7, 20));
        user5AppDate.setId(5L);

        userService.addUser(user5);
        userService.updateUser(user5AppDate);

        assertEquals(Optional.of(user5AppDate), userService.findByIdUser(5L));
    }


    @Test
    void createFriendTest() {
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);

        assertEquals(userStorage.getUsers().stream().filter(
                x -> Objects.equals(x.getId(), 1L)).collect(Collectors.toSet()),
                userStorage.findFriendsByIdUser(3L));
        assertEquals(userStorage.getUsers().stream().filter(
                x -> Objects.equals(x.getId(), 1L)).collect(Collectors.toSet()),
                userStorage.findFriendsByIdUser(2L));
    }

    @Test
    void deleteFriendTest() {
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(1, 2);
        userStorage.addFriend(2, 1);

        HashMap<Long, User> friendsTest = new HashMap<>();
        friendsTest.put(1L, userStorage.findUserById(3L).get());
        friendsTest.put(2L, userStorage.findUserById(2L).get());

        HashMap<Long, User> friendsTest2 = new HashMap<>();
        friendsTest2.put(1L, userStorage.findUserById(1L).get());


        assertEquals(friendsTest.values().stream().collect(Collectors.toSet()),
                userStorage.findFriendsByIdUser(1L));
        assertEquals(friendsTest2.values().stream().collect(Collectors.toSet()),
                userStorage.findFriendsByIdUser(2L));
    }

    @Test
    void findAllGenerateFriends() {
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(1, 4);
        userStorage.addFriend(4, 3);
        userStorage.addFriend(4, 2);

        HashMap<Long, User> friendsTest = new HashMap<>();
        friendsTest.put(1L, userStorage.findUserById(2L).get());
        friendsTest.put(2L, userStorage.findUserById(3L).get());

        assertEquals(friendsTest.values().stream().collect(Collectors.toSet()),
                userStorage.findAllGenerateFriends(1, 4));
        assertEquals(friendsTest.values().stream().collect(Collectors.toSet()),
                userStorage.findAllGenerateFriends(4, 1));
    }

    @Test
    void findByIdUserTest() {
        User user5 = new User();
        user5.setEmail("mailTest@mail.ru");
        user5.setName("nameForTest");
        user5.setLogin("LoginForTest");
        user5.setBirthday(LocalDate.of(2011, 7, 20));

        userService.addUser(user5);

        assertEquals(user5, userService.findByIdUser(5).get());
    }

    @Test
    void findByIdUserFalseTest() {
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.findByIdUser(35);
            }
        });

        assertEquals("Пользователь с id = 35 не найден.", ex.getMessage());
    }

    @Test
    void findAll() {
        User user5 = new User();
        user5.setEmail("mailTest@mail.ru");
        user5.setName("nameForTest");
        user5.setLogin("LoginForTest");
        user5.setBirthday(LocalDate.of(2011, 7, 20));

        userService.addUser(user5);

        assertEquals(userStorage.getUsers(), userService.findAll());
    }
}
