package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Data
public abstract class Repository {
   /* private HashMap<Integer, Object> repository;
    private int id = 0;

    public int generateId() {
        return ++id;
    }

    public void save(Object o) {
        if (repository.containsKey(repository.getId())) {
            repository.put(o.getId(), o);
        } else {
            user.setId(generateId());
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        }
    }*/
}