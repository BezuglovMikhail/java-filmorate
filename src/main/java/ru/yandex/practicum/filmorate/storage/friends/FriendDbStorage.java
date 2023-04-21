package ru.yandex.practicum.filmorate.storage.friends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Repository
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    private final Logger log = LoggerFactory.getLogger(FriendDbStorage.class);

    public FriendDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userDbStorage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userStorage.validateUser(userId) && userStorage.validateUser(friendId)) {


        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)", userId, friendId, false);
        log.info("Пользователь с id= " + friendId + " добавлен в друзья пользователю с id= " + userId);
        } else {
            throw new NotFoundException("Нет пользователя с id= " + userId + " или " + friendId);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        if (userStorage.validateUser(userId) && userStorage.validateUser(friendId)) {
            jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
            log.info("Пользователь с id= " + friendId + " удален из друзей пользователя с id= " + userId);
        } else {
            throw new NotFoundException("Нет пользователя с id= " + userId + " или " + friendId);
        }
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        if (userStorage.validateUser(userId)) {
            return jdbcTemplate.query("SELECT friend_id, email, login, user_name, birthday " +
                            "FROM friends AS f " +
                            "JOIN users AS u ON f.friend_id = u.user_id " +
                            "WHERE f.user_id = ? ", (rs, rowNum) -> new User(
                            rs.getLong("friend_id"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("user_name"),
                            rs.getDate("birthday").toLocalDate()),
                    userId
            );
        } else {
            throw new NotFoundException("Пользователь с id= " + userId + " не найден");
        }
    }
}
