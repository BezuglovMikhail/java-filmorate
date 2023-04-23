package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendDbStorageTest {

    private final UserDbStorage userStorage;

    private final FriendDbStorage friendStorage;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void addFriend() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.saveUser(user2);

        User user3 = new User();
        user3.setEmail("mail_test2@mail.ru");
        user3.setName("Logan");
        user3.setLogin("Log_test");
        user3.setBirthday(LocalDate.of(1998, 9, 7));

        userStorage.saveUser(user3);

        friendStorage.addFriend(1L, 2L);
        friendStorage.addFriend(1L, 3L);
        friendStorage.addFriend(3L, 1L);

        LinkedHashMap<Long, User> friendsTest = new LinkedHashMap();
        friendsTest.put(1L, user2);
        friendsTest.put(2L, user3);

        LinkedHashMap<Long, User> friends1Test = new LinkedHashMap();
        friends1Test.put(1L, user1);

        LinkedHashMap<Long, User> friends0Test = new LinkedHashMap();


        assertEquals(friendsTest.values().stream().collect(Collectors.toList()),
                friendStorage.getFriends(1L).stream().collect(Collectors.toList()));
        assertEquals(friends1Test.values().stream().collect(Collectors.toList()),
                friendStorage.getFriends(3L).stream().collect(Collectors.toList()));
        assertEquals(friends0Test.values().stream().collect(Collectors.toList()),
                friendStorage.getFriends(2L).stream().collect(Collectors.toList()));

    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void removeFriend() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setName(null);
        user1.setLogin("Log");
        user1.setBirthday(LocalDate.of(2000, 11, 6));

        userStorage.saveUser(user1);

        User user2 = new User();
        user2.setEmail("mail_test@mail.ru");
        user2.setName("Log");
        user2.setLogin("Log_test");
        user2.setBirthday(LocalDate.of(1995, 11, 7));

        userStorage.saveUser(user2);

        User user3 = new User();
        user3.setEmail("mail_test2@mail.ru");
        user3.setName("Logan");
        user3.setLogin("Log_test");
        user3.setBirthday(LocalDate.of(1998, 9, 7));

        userStorage.saveUser(user3);

        friendStorage.addFriend(1L, 2L);
        friendStorage.addFriend(1L, 3L);
        friendStorage.addFriend(3L, 1L);
        friendStorage.removeFriend(1L, 2L);

        LinkedHashMap<Long, User> friendsTest = new LinkedHashMap();
        friendsTest.put(2L, user3);

        assertEquals(friendsTest.values().stream().collect(Collectors.toList()),
                friendStorage.getFriends(1L).stream().collect(Collectors.toList()));
    }
}
