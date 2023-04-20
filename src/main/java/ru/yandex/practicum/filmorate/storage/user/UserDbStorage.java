package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long generateId() {
        return 0;
    }

    @Override
    public Optional<User> saveUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.info("Пользователь с идентификатором {} добавлен.", user.getId());
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (validateUser(user.getId())) {
            jdbcTemplate.update("UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Данные пользователя с идентификатором {} обновлены.", user.getId());
            return Optional.of(user);
        } else {
            throw new UserNotFoundException("Пользователя с id = " + user.getId() + " нет.");
        }
    }

    @Override
    public boolean validateUser(Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        return userRows.next();
    }

    @Override
    public Collection<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        ));
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            log.info("Найден пользователь: {} {}", userRows.getString("user_id"), userRows.getString("user_name"));
            return Optional.of(user);
        } else {
            throw new UserNotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    @Override
    public User removeUser(User user) {
        if (validateUser(user.getId())) {
            jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", user.getId());
            log.info("Удален пользователь с: id={}", user.getId());
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователя с id = " + user.getId() + " нет."));
        }
    }
}
