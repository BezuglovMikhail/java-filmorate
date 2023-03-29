package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
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

        assertEquals(user5, userStorage.getUsers().get(5L));
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
        userService.addUser(user5AppDate);

        assertEquals(user5AppDate, userStorage.getUsers().get(5L));
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

    @Test
    void findByIdUserTest() {
        User user5 = new User();
        user5.setEmail("mailTest@mail.ru");
        user5.setName("nameForTest");
        user5.setLogin("LoginForTest");
        user5.setBirthday(LocalDate.of(2011, 7, 20));

        userService.addUser(user5);

        assertEquals(userStorage.getUsers().get(3L), userService.findByIdUser(3));
        assertEquals(user5, userService.findByIdUser(5));
    }

    @Test
    void findByIdUserFalseTest() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, new Executable() {
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

        assertEquals(userStorage.getUsers().values(), userService.findAll());
    }
}
