package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Data
public class UserRepository {
    private HashMap<Integer, User> users = new HashMap<>();
    private int idUser = 0;

    public int generateId() {
        return ++idUser;
    }

    public void saveUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else if (!users.containsKey(user.getId()) && user.getId() != 0) {
            throw new ValidationException("Пользователя с id = " + user.getId() + " нет.");
        } else {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(generateId());
            users.put(user.getId(), user);
        }
    }
}
