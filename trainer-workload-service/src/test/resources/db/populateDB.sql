DELETE FROM trainer;
DELETE FROM training;

INSERT INTO trainer (username, first_name, last_name, is_active)
VALUES ('Tom.Cruise', 'Tom', 'Cruise', true),
       ('Brad.Pitt', 'Brad', 'Pitt', true),
       ('Jennifer.Aniston', 'Jennifer', 'Aniston', true),
       ('Sandra.Bullock', 'Sandra', 'Bullock', true);

INSERT INTO training (trainer_username, date, duration)
VALUES ('Sandra.Bullock',  '2024-01-01', 60),
       ('Brad.Pitt', '2024-01-02', 60),
       ('Brad.Pitt', '2024-01-02', 60),
       ('Jennifer.Aniston', '2024-01-05', 60),
       ('Brad.Pitt', '2024-02-05', 60),
       ('Sandra.Bullock', '2024-01-01', 60),
       ('Tom.Cruise', '2024-01-06', 60),
       ('Jennifer.Aniston', '2024-01-05', 60);