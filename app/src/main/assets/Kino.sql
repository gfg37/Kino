--
-- Файл сгенерирован с помощью SQLiteStudio v3.4.4 в Вс май 19 16:37:09 2024
--
-- Использованная кодировка текста: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Таблица: Films
CREATE TABLE IF NOT EXISTS Films (id, "Название фильма", описание, жанр);

-- Таблица: Sessions
CREATE TABLE IF NOT EXISTS Sessions (id, "id фильма", "дата и время начала", "доступно билетов");

-- Таблица: Tickets
CREATE TABLE IF NOT EXISTS Tickets (id, "id пользователя", "id фильма", "количество билетов");

-- Таблица: Users
CREATE TABLE IF NOT EXISTS Users (id, Имя, Роль);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
