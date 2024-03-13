# java-filmorate-db
Проектирование базы данных для filmorate.

>[!NOTE]
>Краткое изложение сути:
>есть класс User, поля видно в таблице. Пользователь может добавить другого пользователя в друзья, но нужно подтверждение для этого вводится friendStatus в таблице friends
Есть класс фильм, поля также в таблице. Фильму можно поставить лайк, для этого я подготовил таблицу likers.

**[ER-диаграмма](https://dbdiagram.io/d/filmorateDB-65ef314bb1f3d4062ca4cb98):**
![image](https://github.com/codeByDope/java-filmorate/assets/140439291/64f27adc-5b3f-42b8-9001-2883102d6081)


***Примеры запросов:***
1. Получение всех пользователей
```
SELECT *
FROM users;
```
2. Получение всех фильмов
```
SELECT *
FROM films;
```
3. Получение друзей определенного пользователя
```
SELECT friend_user_id
FROM friends
WHERE main_user_id = 9; --Сюда можно подставить любой ID
```
4. Получение таблицы "Название фильма - количество лайков" с топ-10 популярными фильмами
```
SELECT f.title,
       COUNT(liker_id) AS count_likes
FROM likers
INNER JOIN films AS f ON likers.film_id = f.id
GROUP BY f.title
ORDER BY count_likes DESC;
```
5. Получение таблицы "ID пользователя - количество друзей"
```
SELECT u.id,
       COUNT(friend_user_id) AS count_friends
FROM friends
RIGHT OUTER JOIN users AS u ON friends.main_user_id = u.id
GROUP BY u.id
```

6. Получение таблицы id и логина друзей пользователя с id N
```
SELECT u.user_id,
       u.login
FROM user AS u
LEFT JOIN friends AS l ON u.user_id = f.friend_user_id
WHERE f.main_user_id = 9 -- тут может быть любой ID
  AND f.is_friend = true;
```

7. Получение таблицы с id пользователей, являющимися общими друзьями для определенных юзеров
```
SELECT u.user_id
FROM user AS u
LEFT JOIN friends AS f1 ON u.user_id = f1.friend_user_id
LEFT JOIN friends AS f2 ON u.user_id = f2.friend_user_id
WHERE (f1.main_user_id = 3 -- Любой нужный ID
       AND f1.is_friend = true)
  AND (f2.main_user_id = 7
       AND f2.is_friend = true);
```
