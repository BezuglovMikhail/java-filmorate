package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.h2.command.dml.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@Data
class UserStorageTest {
    public InMemoryUserStorage userStorage = new InMemoryUserStorage();

    @Test
    void generateIdTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);
        assertEquals(1, userStorage.findUserById(1L).get().getId());
        assertEquals("Nik", userStorage.findUserById(1L).get().getName());
        assertEquals("Log", userStorage.findUserById(1L).get().getLogin());
        assertEquals("mail@mail.ru", userStorage.findUserById(1L).get().getEmail());
        assertEquals(LocalDate.of(2000, 11, 6),
                userStorage.findUserById(1L).get().getBirthday());
    }

    @Test
    void saveUserTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName(null);
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user);
        assertEquals(1, userStorage.findUserById(1L).get().getId());
        assertEquals("Log", userStorage.findUserById(1L).get().getName());
        assertEquals("Log", userStorage.findUserById(1L).get().getLogin());
        assertEquals("mail@mail.ru", userStorage.findUserById(1L).get().getEmail());
        assertEquals(LocalDate.of(2000, 11, 6),
                userStorage.findUserById(1L).get().getBirthday());
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

        userStorage.updateUser(user2);

        assertEquals(1, userStorage.findUserById(1L).get().getId());
        assertEquals("Nik345", userStorage.findUserById(1L).get().getName());
        assertEquals("Login", userStorage.findUserById(1L).get().getLogin());
        assertEquals("maillllll@mail.ru", userStorage.findUserById(1L).get().getEmail());
        assertEquals(LocalDate.of(2000, 11, 16),
                userStorage.findUserById(1L).get().getBirthday());
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

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userStorage.updateUser(user2);
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

        HashMap<Long, User> usersTest = new HashMap();
        usersTest.put(1L, user);
        usersTest.put(2L, user2);

        assertEquals(usersTest.values().stream().collect(Collectors.toSet()),
                userStorage.getUsers().stream().collect(Collectors.toSet()));
        assertEquals(2, userStorage.getUsers().size());
    }
}
