--- Удаление таблицы users
DROP TABLE IF EXISTS users;
--- Создание таблицы users
CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    user_name  VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    passport   INT         NOT NULL UNIQUE,
    email      VARCHAR(50),
    bank_card  BIGINT      NOT NULL
    );

--- Инициализация таблицы users
INSERT INTO users (user_name, first_name, last_name, passport, email, bank_card)
VALUES ('bill', 'Ivanov', 'Dima', 456987123, null, 7896541236547852),
       ('tom', 'Sidorov', 'Pasha', 98741236, null, 987456321458796),
       ('jerry', 'Petrov', 'Sasha', 12365478, null, 12589745321698),
       ('ozi', 'Galcin', 'Gena', 56987415, 'gav@mail.ru', 32569874125463),
       ('eminem', 'Pugachev', 'Genya', 85297418, null, 943655557412365);

--- Удаление таблицы bikes
DROP TABLE IF EXISTS bikes;

--- Создание таблицы bikes
CREATE TABLE IF NOT EXISTS bikes
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50)      NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    horse_power INT              NOT NULL,
    volume      DOUBLE PRECISION NOT NULL,
    user_id      INT
);

--- Инициализация таблицы bikes
INSERT INTO bikes (name, price, horse_power, volume, user_id)
VALUES ('BMW', 2000, 200, 1.0, null),
       ('SUZUKI', 30000, 300, 1.0, null),
       ('YAMAHA', 40000, 400, 1.0, null),
       ('URAL', 2000, 200, 1.0, null),
       ('HONDA', 2000, 200, 1.0, null);

--- Удаление таблицы cars
DROP TABLE IF EXISTS cars;

--- Создание таблицы cars
CREATE TABLE IF NOT EXISTS cars
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(50)      NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    horse_power INT              NOT NULL,
    volume      DOUBLE PRECISION NOT NULL,
    color       VARCHAR(50)      NOT NULL,
    user_id      INT
);

--- Инициализация таблицы cars
INSERT INTO cars (title, price, horse_power, volume, color, user_id)
VALUES ('MERCEDES', 655.30, 250, 3.5, 'black', null),
       ('HONDA', 360.50, 190, 2.4, 'red', null),
       ('HYUNDAI', 320.90, 156, 2.0, 'white', null),
       ('BMW', 640.50, 450, 5.0, 'blue', null),
       ('OPEL', 210.90, 110, 1.8, 'gold', null);



