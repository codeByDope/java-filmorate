# Filmorate

Данный проект - это сервис, который дает возможность пользователям выбирать, комментировать и оценивать фильмы, а также искать наиболее популярные среди них. Также, на сервисе можно добавлять друзей и получать рекомендации на основе их лайков.

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

3. Получение друзей определенного пользователя
```
SELECT friend_user_id
FROM friends
WHERE main_user_id = 9; --Сюда можно подставить любой ID

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

7. Поставить лайк фильму
```
INSERT INTO likers VALUES (?, ?)
```

8. Получение всех режиссеров
```
SELECT*
FROM directors
```

9. Получение режиссера по id
```
SELECT*
FROM directors
WHERE id = ?
```

10. Добавить режиссера
```
INSERT into directors (name) values (?)
```
11. Обновить режиссера по id
```
UPDATE directors SET name = ?
WHERE id = ?
```

12. Удалить режиссера по id
```
DELETE FROM directors
WHERE id = ?
```

13. Получить все фильмы режиссера
```
SELECT *
FROM directors as d " +
            "JOIN films_directors as fd on fd.director_id = d.id " +
            "JOIN films as f on f.id = fd.film_id " +
            "WHETE f.id = ? " +
            "ORDER by fd.director_id
```

14. Удаление пользователя по id
```
DELETE FROM users
WHERE id = ?
```

15. Удаление фильма по id
```
DELETE FROM films
WHERE id = ?
```

16. Получение ленты событий
```
SELECT *
FROM feed
WHERE user_id = ?
```

17. Получение фильма по id
```
SELECT*
FROM films
WHERE id = ?
```

18. Получение списка самых популярных фильмов
```
SELECT f.*
FROM films AS f
LEFT JOIN likers AS l ON l.film_id = f.id 
WHERE LOWER(f.title) LIKE LOWER(?) 
GROUP BY f.id 
ORDER BY COUNT(l.film_id) DESC
```

19. Получение общих фильмов
```
SELECT f.*, COUNT(l.film_id) AS like_count
FROM films AS f
JOIN likers AS l ON f.id = l.film_id 
WHERE l.liker_id = ? 
AND l.film_id IN (
    SELECT l.film_id 
    FROM likers l 
    WHERE l.liker_id = ?) 
GROUP BY f.id 
ORDER BY like_count DESC; 
```

20. Удаление пользователя из списка друзей
```
DELETE FROM friends
WHERE main_user_id = ? AND friend_user_id = ?
```

21. Получение общих друзей
```
SELECT *
FROM users AS u
LEFT JOIN friends AS f1 ON u.id = f1.friend_user_id
LEFT JOIN friends AS f2 ON u.id = f2.friend_user_id
WHERE f1.main_user_id = ?
AND f2.main_user_id = ?
```

22. Получение списка всех жанров
```
SELECT *
FROM genres
```

23. Получение названий жанров
```
SELECT *
FROM genres
WHERE id = ?
```

24. Убрать лайк с фильма
```
DELETE FROM likers
WHERE film_id = ? AND liker_id = ?
```

25. Получение списка всех рейтингов
```
SELECT * FROM MPA_ratings
```

26. Получение названия рейтинга по id
```
SELECT *
FROM MPA_ratings
WHERE id = ?
```

27. Добавление отзыва
```
INSERT INTO review (user_id, film_id, content, is_positive, useful)
VALUES (?, ?, ?, ?, ?)
```

28. Обновление отзыва
```
UPDATE review
SET content=?, is_positive=?
WHERE id=?
```

29. Удаление отзыва
```
DELETE FROM review
WHERE id=?
```
30. Получение отзыва по id
```
SELECT r.*
COALESCE(l.like_count, 0) AS like_count 
COALESCE(d.dislike_count, 0) AS dislike_count 
FROM review r 
LEFT JOIN (SELECT review_id, COUNT(DISTINCT user_id) AS like_count FROM likes GROUP BY review_id) l ON r.id = l.review_id
LEFT JOIN (SELECT review_id, COUNT(DISTINCT user_id) AS dislike_count FROM dislikes GROUP BY review_id) d ON r.id = d.review_id
WHERE r.id = %s
```

31. Поставить лайк отзыву
```
INSERT INTO likes (review_id, user_id)
VALUES (?, ?)
```

32. Поставить дизлайк отзыву
```
INSERT INTO dislikes (review_id, user_id)
VALUES (?, ?)
```

33. Убрать лайк с отзыва
```
DELETE FROM likes
WHERE review_id=? AND user_id=?
```

34. Убрать дизлайк с отзыва
```
DELETE FROM dislikes
WHERE review_id=? AND user_id=?
```

## Технологический стек

- Java 11
- Spring boot 2
- JDBC, SQL, H2
- Apache Maven
