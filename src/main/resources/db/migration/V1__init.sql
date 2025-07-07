CREATE TABLE users (
                        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        name VARCHAR(500),
                        date_of_birth DATE,
                        password VARCHAR(500)
);

CREATE TABLE account (
                         id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                         user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
                         balance DECIMAL(19,2),
                         initial_balance DECIMAL(19,2) NOT NULL
);

CREATE TABLE email_data (
                            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                            user_id BIGINT REFERENCES users(id) NOT NULL ,
                            email VARCHAR(200) UNIQUE NOT NULL
);

CREATE TABLE phone_data (
                            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                            user_id BIGINT REFERENCES users(id) NOT NULL,
                            phone VARCHAR(13) UNIQUE NOT NULL
);

CREATE INDEX idx_email_data_email ON email_data(email);
CREATE INDEX idx_phone_data_phone ON phone_data(phone);
CREATE INDEX idx_users_date_of_birth ON users(date_of_birth);

-- Пользователи
INSERT INTO users (name, date_of_birth, password) VALUES
                                                      ('Иван Иванов', TO_DATE('15.06.1985', 'DD.MM.YYYY'), 'password123'),
                                                      ('Мария Петрова', TO_DATE('01.12.1990', 'DD.MM.YYYY'), 'securePass!'),
                                                      ('Алексей Смирнов', TO_DATE('22.03.1978', 'DD.MM.YYYY'), 'myPass2024'),
                                                      ('Ольга Кузнецова', TO_DATE('10.09.1995', 'DD.MM.YYYY'), 'passOlga95'),
                                                      ('Дмитрий Соколов', TO_DATE('05.11.1982', 'DD.MM.YYYY'), 'dmitry1982'),
                                                      ('Елена Новикова', TO_DATE('20.01.1988', 'DD.MM.YYYY'), 'novaElena88'),
                                                      ('Сергей Морозов', TO_DATE('18.04.1975', 'DD.MM.YYYY'), 'morozov75'),
                                                      ('Анна Федорова', TO_DATE('30.07.1993', 'DD.MM.YYYY'), 'annaF93'),
                                                      ('Павел Васильев', TO_DATE('12.12.1980', 'DD.MM.YYYY'), 'pavelV80'),
                                                      ('Наталья Орлова', TO_DATE('25.03.1991', 'DD.MM.YYYY'), 'nataliaO91');

-- Аккаунты с балансом и начальным депозитом
INSERT INTO account (user_id, balance, initial_balance) VALUES
                                                            (1, 1000.00, 1000.00),
                                                            (2, 5000.00, 5000.00),
                                                            (3, 750.00, 750.00),
                                                            (4, 1200.00, 1200.00),
                                                            (5, 3000.00, 3000.00),
                                                            (6, 2000.00, 2000.00),
                                                            (7, 4500.00, 4500.00),
                                                            (8, 1800.00, 1800.00),
                                                            (9, 2200.00, 2200.00),
                                                            (10, 1600.00, 1600.00);

-- Email — у некоторых по 2 адреса
INSERT INTO email_data (user_id, email) VALUES
                                            (1, 'ivan.ivanov@example.com'),
                                            (1, 'ivanov.i@example.org'),
                                            (2, 'maria.petrova@example.com'),
                                            (3, 'alexey.smirnov@example.com'),
                                            (4, 'olga.kuznetsova@example.com'),
                                            (5, 'dmitry.sokolov@example.com'),
                                            (5, 'd.sokolov@example.net'),
                                            (6, 'elena.novikova@example.com'),
                                            (7, 'sergey.morozov@example.com'),
                                            (8, 'anna.fedorova@example.com'),
                                            (8, 'anna.f@example.org'),
                                            (9, 'pavel.vasiliev@example.com'),
                                            (10, 'natalia.orlova@example.com');

-- Телефоны — у некоторых по 2 номера
INSERT INTO phone_data (user_id, phone) VALUES
                                            (1, '79201234567'),
                                            (1, '79209876543'),
                                            (2, '79207654321'),
                                            (3, '79205551234'),
                                            (4, '79203334455'),
                                            (5, '79204445566'),
                                            (5, '79207778899'),
                                            (6, '79206667788'),
                                            (7, '79209998877'),
                                            (8, '79201112233'),
                                            (8, '79202223344'),
                                            (9, '79205556677'),
                                            (10, '79208889900');