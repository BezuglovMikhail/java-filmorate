package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Sql({"/schema.sql", "/data.sql"})
    @Test
    void saveAndFindUserByIdUserTest() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        Optional<User> userOptional = userStorage.findUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("email", "mail@mail.ru")
                                .hasFieldOrPropertyWithValue("name", "Log")
                                .hasFieldOrPropertyWithValue("login", "Log")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 11, 6))
                );
    }

    @Sql({"/schema.sql", "/data.sql"})
    @Test
    void updateUserTest() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.updateUser(user2);

        Optional<User> userOptional = userStorage.findUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("email", "mail_test@mail.ru")
                        .hasFieldOrPropertyWithValue("name", "Log")
                        .hasFieldOrPropertyWithValue("login", "Log_test")
                        .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1995, 11, 7))
                );
    }

    @Sql({"/schema.sql", "/data.sql"})
    @Test
    void validateUserTest() {

        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setId(1000L);
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userStorage.updateUser(user2);
            }
        });

        assertEquals("Пользователя с id = 1000 нет.", ex.getMessage());
    }

    @Sql({"/schema.sql", "/data.sql"})
    @Test
    void getUsersTest() {
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

    @Sql({"/schema.sql", "/data.sql"})
    @Test
    void removeUserTest() {
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
        user.setId(1L);
        userStorage.removeUser(user);

        HashMap<Long, User> usersTest = new HashMap();
        usersTest.put(2L, user2);

        assertEquals(usersTest.values().stream().collect(Collectors.toSet()),
                userStorage.getUsers().stream().collect(Collectors.toSet()));
        assertEquals(1, userStorage.getUsers().size());
    }
}
