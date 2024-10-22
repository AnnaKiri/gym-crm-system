DROP TABLE IF EXISTS training;
DROP TABLE IF EXISTS trainee2trainer;
DROP TABLE IF EXISTS trainee;
DROP TABLE IF EXISTS trainer;
DROP TABLE IF EXISTS login_attempt;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS training_type;

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255)         NOT NULL,
    last_name  VARCHAR(255)         NOT NULL,
    username   VARCHAR(255)         NOT NULL,
    is_active  BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT unique_username UNIQUE (username)
);
CREATE UNIQUE INDEX users_unique_username_idx ON users (username);

CREATE TABLE training_type
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE trainee
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_of_birth DATE,
    address       VARCHAR(255),
    user_id       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE trainer
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    specialization_id BIGINT NOT NULL,
    user_id           BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (specialization_id) REFERENCES training_type (id) ON DELETE NO ACTION
);

CREATE TABLE training
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    trainee_id BIGINT       NOT NULL,
    trainer_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    type_id    BIGINT       NOT NULL,
    date       DATE         NOT NULL,
    duration   INTEGER      NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE NO ACTION,
    FOREIGN KEY (type_id) REFERENCES training_type (id) ON DELETE NO ACTION
);

CREATE TABLE trainee2trainer
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    trainee_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE CASCADE
);

CREATE TABLE login_attempt
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(255) NOT NULL,
    attempts      INTEGER      NOT NULL,
    blocked_until TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);
