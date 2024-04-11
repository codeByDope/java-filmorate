MERGE INTO genres(id, title) VALUES (1, 'Комедия');
MERGE INTO genres(id, title) VALUES (2, 'Драма');
MERGE INTO genres(id, title) VALUES (3, 'Мультфильм');
MERGE INTO genres(id, title) VALUES (4, 'Триллер');
MERGE INTO genres(id, title) VALUES (5, 'Документальный');
MERGE INTO genres(id, title) VALUES (6, 'Боевик');

MERGE INTO mpa_ratings(id, title) VALUES (1, 'G');
MERGE INTO mpa_ratings(id, title) VALUES (2, 'PG');
MERGE INTO mpa_ratings(id, title) VALUES (3, 'PG-13');
MERGE INTO mpa_ratings(id, title) VALUES (4, 'R');
MERGE INTO mpa_ratings(id, title) VALUES (5, 'NC-17');