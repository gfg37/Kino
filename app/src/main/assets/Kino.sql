--
-- ���� ������������ � ������� SQLiteStudio v3.4.4 � �� ��� 19 16:37:09 2024
--
-- �������������� ��������� ������: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- �������: Films
CREATE TABLE IF NOT EXISTS Films (id, "�������� ������", ��������, ����);

-- �������: Sessions
CREATE TABLE IF NOT EXISTS Sessions (id, "id ������", "���� � ����� ������", "�������� �������");

-- �������: Tickets
CREATE TABLE IF NOT EXISTS Tickets (id, "id ������������", "id ������", "���������� �������");

-- �������: Users
CREATE TABLE IF NOT EXISTS Users (id, ���, ����);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
