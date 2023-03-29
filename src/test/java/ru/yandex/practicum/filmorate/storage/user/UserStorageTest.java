package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {
    InMemoryUserStorage userStorage = new InMemoryUserStorage();

    @Test
    void generateIdTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);
        assertEquals(1, userStorage.getUsers().get(1L).getId());
        assertEquals("Nik", userStorage.getUsers().get(1L).getName());
        assertEquals("Log", userStorage.getUsers().get(1L).getLogin());
        assertEquals("mail@mail.ru", userStorage.getUsers().get(1L).getEmail());
        assertEquals(LocalDate.of(2000, 11, 6),
                userStorage.getUsers().get(1L).getBirthday());
    }

    @Test
    void saveUserTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName(null);
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);
        assertEquals(1, userStorage.getUsers().get(1L).getId());
        assertEquals("Log", userStorage.getUsers().get(1L).getName());
        assertEquals("Log", userStorage.getUsers().get(1L).getLogin());
        assertEquals("mail@mail.ru", userStorage.getUsers().get(1L).getEmail());
        assertEquals(LocalDate.of(2000, 11, 6),
                userStorage.getUsers().get(1L).getBirthday());
    }

    @Test
    void saveUserAppDateTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);

        User user2 = new User();
        user2.setId(1);
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(2000, 11, 16));

        userStorage.saveUser(user2);

        assertEquals(1, userStorage.getUsers().get(1L).getId());
        assertEquals("Nik345", userStorage.getUsers().get(1L).getName());
        assertEquals("Login", userStorage.getUsers().get(1L).getLogin());
        assertEquals("maillllll@mail.ru", userStorage.getUsers().get(1L).getEmail());
        assertEquals(LocalDate.of(2000, 11, 16),
                userStorage.getUsers().get(1L).getBirthday());
    }

    @Test
    void validateUserAppDateFalseTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);

        User user2 = new User();
        user2.setId(1000);
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(2000, 11, 16));

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userStorage.saveUser(user2);
            }
        });

        assertEquals("Пользователя с id = 1000 нет.", ex.getMessage());
    }

    @Test
    void getUsersTest() {
        assertEquals(0, userStorage.getUsers().size());
    }

    @Test
    void getUsers2Test() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);

        User user2 = new User();
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(2000, 11, 16));

        userStorage.saveUser(user2);

        HashMap<Long, User> usersTest = new HashMap<>();
        usersTest.put(1L, user);
        usersTest.put(2L, user2);

        assertEquals(usersTest, userStorage.getUsers());
        assertEquals(2, userStorage.getUsers().size());
    }
}
