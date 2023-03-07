package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    //private static final Logger log = LoggerFactory.getLogger(UserController.class);
  UserRepository userRepository = new UserRepository();

    @GetMapping(value = "/users")
    //@ResponseBody
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userRepository.getUsers().size());
        return userRepository.getUsers().values();
    }

    @PostMapping(value = "/users")
    //@ResponseBody
    public User create(@Valid @RequestBody User user) {

   /* if (user.getLogin().contains(" ")) {
        throw new ValidationException("Логин не может содержать пробелы.");
    }
    if (user.getName().isEmpty()) {
        user.setName(user.getLogin());
    }
    if (user.getBirthday().isBefore(LocalDateTime.now())) {
        throw new ValidationException("Дата рождения не может быть в будующем");
    }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }*/
        //if (validUser(user)) {
        //     addUser(user);
        //}
        userRepository.saveUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    // @ResponseBody
    public User appDate(@Valid @RequestBody User user) {
        userRepository.saveUser(user);
        return user;
    }
    /*public boolean validUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы.");
            //return false;
        }
       // if (user.getBirthday().isBefore(LocalDateTime.now())) {
            //throw new ValidationException("Дата рождения не может быть в будующем");
            //return false;
       //}
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            return true;
        }
        return true;
    }*/

}