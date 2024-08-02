DELETE FROM training_type;
DELETE FROM trainee;
DELETE FROM trainer;
DELETE FROM users;
DELETE FROM trainee2trainer;
DELETE FROM training;

INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES ('Angelina', 'Jolie', 'Angelina.Jolie', '{noop}password1', true),
       ('Ryan', 'Reynolds', 'Ryan.Reynolds', '{noop}password2', true),
       ('Tom', 'Hardy', 'Tom.Hardy', '{noop}password3', true),
       ('Keanu', 'Reeves', 'Keanu.Reeves', '{noop}password4', true),
       ('Tom', 'Cruise', 'Tom.Cruise', '{noop}password5', true),
       ('Brad', 'Pitt', 'Brad.Pitt', '{noop}password6', true),
       ('Jennifer', 'Aniston', 'Jennifer.Aniston', '{noop}password7', true),
       ('Sandra', 'Bullock', 'Sandra.Bullock', '{noop}password8', true);

INSERT INTO training_type (name)
VALUES ('Strength'),
       ('Aerobic'),
       ('Yoga'),
       ('Stretching');

INSERT INTO trainee (date_of_birth, address, user_id)
VALUES ('1975-06-04', 'some address', 1),
       ('1976-10-23', 'some address', 2),
       ('1976-09-15', 'some address', 3),
       ('1964-09-02', 'some address', 4);

INSERT INTO trainer (specialization_id, user_id)
VALUES (1, 5),
       (2, 6),
       (3, 7),
       (4, 8);

INSERT INTO training (trainee_id, trainer_id, name, type_id, date, duration)
VALUES (1, 4, 'Stretching', 4, '2024-01-01', 60),
       (1, 2, 'Aerobic', 2, '2024-01-02', 60),
       (2, 2, 'Aerobic', 2, '2024-01-02', 60),
       (2, 3, 'Yoga', 3, '2024-01-05', 60),
       (3, 2, 'Strength', 1, '2024-01-05', 60),
       (4, 4, 'Stretching', 4, '2024-01-01', 60),
       (4, 1, 'Strength', 1, '2024-01-06', 60),
       (2, 3, 'Yoga', 3, '2024-01-05', 60);

INSERT INTO trainee2trainer (trainee_id, trainer_id)
VALUES (1, 4),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 2),
       (4, 4),
       (4, 1);