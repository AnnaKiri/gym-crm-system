DROP TABLE IF EXISTS training;
DROP TABLE IF EXISTS trainee2trainer;
DROP TABLE IF EXISTS trainee;
DROP TABLE IF EXISTS trainer;
DROP TABLE IF EXISTS login_attempt;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS training_type;
DROP TABLE IF EXISTS invalid_token;

CREATE TABLE users
(
    id               BIGINT  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name       VARCHAR                           NOT NULL,
    last_name        VARCHAR                           NOT NULL,
    username         VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    is_active        BOOL                DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_username_idx ON users (username);

CREATE TABLE training_type
(
    id               BIGINT  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name             VARCHAR                           NOT NULL
);

CREATE TABLE trainee
(
    id               BIGINT  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date_of_birth    DATE                                      ,
    address          VARCHAR                                   ,
    user_id          BIGINT                            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE trainer
(
    id                   BIGINT  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    specialization_id    BIGINT                              NOT NULL,
    user_id              BIGINT                              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (specialization_id) REFERENCES training_type (id) ON DELETE NO ACTION
);

CREATE TABLE training
(
    id             BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    trainee_id     BIGINT                           NOT NULL,
    trainer_id     BIGINT                           NOT NULL,
    name           VARCHAR                          NOT NULL,
    type_id        BIGINT                           NOT NULL,
    date           DATE                             NOT NULL,
    duration       INTEGER                          NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE NO ACTION,
    FOREIGN KEY (type_id) REFERENCES training_type (id) ON DELETE NO ACTION
);

CREATE TABLE trainee2trainer
(
    id             BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    trainee_id     BIGINT                           NOT NULL,
    trainer_id     BIGINT                           NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE CASCADE
);

CREATE TABLE login_attempt
(
    id               BIGINT  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username         VARCHAR                            NOT NULL,
    attempts         INTEGER                            NOT NULL,
    blocked_until    TIMESTAMP                                  ,
    FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);

CREATE TABLE invalid_token
(
    id               BIGINT  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    token            VARCHAR                            NOT NULL,
    invalidated_at   TIMESTAMP                          NOT NULL
);