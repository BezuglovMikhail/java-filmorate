package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    /*UserController userController = new UserController();

    @Test
    void createTrueTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userController.create(user);
        assertEquals(1, userController.userRepository.getUsers().get(1).getId());
        assertEquals("Nik", userController.userRepository.getUsers().get(1).getName());
        assertEquals("Log", userController.userRepository.getUsers().get(1).getLogin());
        assertEquals("mail@mail.ru", userController.userRepository.getUsers().get(1).getEmail());
        assertEquals(LocalDate.of(2000, 11, 6),
                userController.userRepository.getUsers().get(1).getBirthday());
    }

    @Test
    void createTrue1Test() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName(null);
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userController.create(user);
        assertEquals(1, userController.userRepository.getUsers().get(1).getId());
        assertEquals("Log", userController.userRepository.getUsers().get(1).getName());
        assertEquals("Log", userController.userRepository.getUsers().get(1).getLogin());
        assertEquals("mail@mail.ru", userController.userRepository.getUsers().get(1).getEmail());
        assertEquals(LocalDate.of(2000, 11, 6),
                userController.userRepository.getUsers().get(1).getBirthday());
    }

    @Test
    void appDateTrueTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userController.create(user);

        User user2 = new User();
        user2.setId(1);
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(2000, 11, 16));

        userController.appDate(user2);

        assertEquals(1, userController.userRepository.getUsers().get(1).getId());
        assertEquals("Nik345", userController.userRepository.getUsers().get(1).getName());
        assertEquals("Login", userController.userRepository.getUsers().get(1).getLogin());
        assertEquals("maillllll@mail.ru", userController.userRepository.getUsers().get(1).getEmail());
        assertEquals(LocalDate.of(2000, 11, 16),
                userController.userRepository.getUsers().get(1).getBirthday());
    }

    @Test
    void appDateFalseTest() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userController.create(user);

        User user2 = new User();
        user2.setId(1000);
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(2000, 11, 16));

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userController.appDate(user2);
            }
        });

        assertEquals("Пользователя с id = 1000 нет.", ex.getMessage());
    }


    @Test
    void findAllTest() {
        assertEquals(0, userController.findAll().size());
    }


    @Test
    void findAll1Test() {

        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("Nik");
        user.setLogin("Log");
        user.setBirthday(LocalDate.of(2000, 11, 6));

        userController.create(user);

        User user2 = new User();
        user2.setEmail("maillllll@mail.ru");
        user2.setName("Nik345");
        user2.setLogin("Login");
        user2.setBirthday(LocalDate.of(2000, 11, 16));

        userController.create(user2);

        HashMap<Integer, User> usersTest = new HashMap<>();
        usersTest.put(1, user);
        usersTest.put(2, user2);

        assertEquals(usersTest, userController.userRepository.getUsers());
        assertEquals(2, userController.findAll().size());
    }*/
}