--- Удаление таблицы bikes
DROP TABLE IF EXISTS bikes;

--- Создание таблицы bikes
CREATE TABLE IF NOT EXISTS bikes
(
    id SERIAL PRIMARY KEY,
    name VARCHAR ( 50 ) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    horse_power INT NOT NULL,
    volume DOUBLE PRECISION NOT NULL
    );

--- Инициализация таблицы bikes
INSERT INTO bikes (name, price, horse_power, volume)
VALUES ('BMW', 2000, 200, 1.0),
       ('SUZUKI', 30000, 300, 1.0),
       ('YAMAHA', 40000, 400, 1.0),
       ('NOT BMW', 2000, 200, 1.0),
       ('NOT SUZUKI', 2000, 200, 1.0);
