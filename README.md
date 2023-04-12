# java-filmorate
## Это репозиторий проекта "Filmorate".

Наше приложение **умеет**:
1. Добавлять фильмы и работать с ними:
   * ставить лайки и удалять их (ставить лайки могут только зарегистрированные пользователи);
   * искать фильмы по id;
   * выдавать список популярных фильмов (согласно полученным лайкам);
   * выдавать список фильмов из базы с заданным лимитом;
2. Добавлять пользователей.
3. Пользователи могут добавлять друг друга в друзья:
   * Пользователь отправляет запрос на добавление в друзья;
   * Для добавления в друзья пользователь должен принять запрос на дружбу;
4. Можно посмотреть список друзей пользователя.
5. Можно посмотреть список общих друзей двух пользователей.

Приложение написано на Java. Пример кода:
```java
public class Practicum {
    public static void main(String[] args) {
    }
}
```
Для хранения данных приложение использует базу данных PostgreSQL.

![Схема базы данных приложения "Filmorate"](/Filmorate_PosgreSQL.png)
Схема базы данных приложения "Filmorate"

Примеры стандартных запросов:
```sql
SELECT * 
FROM users; -- Выведет список пользователей.

SELECT * 
FROM users
WHERE user_id = 3; -- Выведет данные пользователя с id = 3.

SELECT * 
FROM films; -- Выведет список фильмов.

SELECT * 
FROM films 
WHERE film_id = 2;  -- Выведет данные фильма с id = 2.

SELECT u.user_id,
       u.email, 
       u.login, 
       u.user_name, 
       u.birthday  
FROM users u JOIN friends f ON u.user_id = f.user_id
WHERE f.friend_id = 1
AND f.status_friendship = 'друзья'; -- Выведет список друзей пользователя с id = 1.

SELECT u.user_id, 
       u.email, 
       u.login, 
       u.user_name, 
       u.birthday
FROM (SELECT *
      FROM friends f JOIN friends f2 USING(friend_id)
      WHERE f.user_id = 1 
      AND f2.user_id  = 2 
      AND f.status_friendship = 'друзья' 
      AND f2.status_friendship = 'друзья') AS common 
      JOIN users u ON common.friend_id = u.user_id; -- Выведет список общих друзей пользователя с id = 1 и id = 2.

SELECT f.film_name, 
       COUNT(l.film_id) 
FROM films f JOIN likes l ON f.film_id = l.film_id 
GROUP BY f.film_name 
ORDER BY COUNT(l.film_id) DESC; -- Выведет список фильмов и количество лайков (начиная с самого популярного).

SELECT f.film_name 
FROM films f JOIN likes l ON f.film_id = l.film_id  
GROUP BY f.film_name 
ORDER BY COUNT(l.film_id) DESC 
LIMIT 1; -- Выведет самый популярный фильм.

SELECT g.ganre_name 
FROM films f JOIN films_ganres fg ON f.film_id = fg.film_id 
JOIN ganres g ON fg.ganre_id = g.ganre_id 
WHERE f.film_id = 2; -- Выведет жанры фильма с id = 2.
```

------
О том, как научиться создавать такие приложения, можно узнать в [Яндекс-Практикуме](https://practicum.yandex.ru/java-developer/ "Тут учат Java!")
=======
