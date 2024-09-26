INSERT INTO users(id, name, username, email) VALUES (1, 'Farangiz', 'farangizn', 'farangizhon2004@icloud.com')
INSERT INTO post(id, title, body, user_id) VALUES (1, 'post 1', 'body 1', 1);
INSERT INTO post(id, title, body, user_id) VALUES (2, 'post 2', 'body 2', 1);

ALTER SEQUENCE  hibernate_sequence RESTART WITH 3;