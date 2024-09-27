DELETE FROM trainer;
DELETE FROM summary;

INSERT INTO trainer (username, first_name, last_name, is_active)
VALUES ('Tom.Cruise', 'Tom', 'Cruise', true),
       ('Brad.Pitt', 'Brad', 'Pitt', true),
       ('Jennifer.Aniston', 'Jennifer', 'Aniston', true),
       ('Sandra.Bullock', 'Sandra', 'Bullock', true);

INSERT INTO summary (trainer_username, training_year, training_month,  duration)
VALUES ('Sandra.Bullock', 2024, 1, 60),
       ('Sandra.Bullock',  2024, 2, 60),
       ('Brad.Pitt', 2024, 1, 120),
       ('Brad.Pitt', 2024, 2, 60),
       ('Jennifer.Aniston', 2024, 1, 120),
       ('Tom.Cruise', 2024, 1, 60);
