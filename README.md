# java-filmorate-db
Проектирование базы данных для filmorate.

>[!NOTE]
>Краткое изложение сути:
>есть класс User, поля видно в таблице. Пользователь может добавить другого пользователя в друзья, но нужно подтверждение для этого вводится friendStatus в таблице friends
Есть класс фильм, поля также в таблице. Фильму можно поставить лайк, для этого я подготовил таблицу likers.

**[ER-диаграмма](https://dbdiagram.io/d/filmorateDB-65ef314bb1f3d4062ca4cb98):**
![image](https://github.com/codeByDope/java-filmorate/assets/140439291/a51d607d-7bba-4f31-911f-8040ae94b5ca)

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
