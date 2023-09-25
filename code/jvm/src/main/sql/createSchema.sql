drop table if EXISTS Users cascade;
drop table if EXISTS Tokens cascade;
drop table if EXISTS Games cascade;
drop table if EXISTS Lobby cascade;

create table Users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email    varchar(60) NOT NULL CHECK (email SIMILAR TO '_%@_%') UNIQUE,
    password CHAR(64)    NOT NULL CHECK ( char_length(password) = 64 ),
    points   INT NOT NULL CHECK (points >= 0) DEFAULT 0,
    nr_games_played   INT NOT NULL CHECK (nrGamesPlayed >= 0) DEFAULT 0
    );

create table Tokens
(
    token_validation VARCHAR(256) primary key,
    user_id          INT REFERENCES Users (id),
    created_at       DATE NOT NULL,
    last_used_at     DATE NOT NULL
);


create table Games
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(50) NOT NULL,
    user_id   INT REFERENCES Users (id),
    grid_size INT         NOT NULL,
    variant   VARCHAR(50) NOT NULL
);

create table Lobby
(
    id         SERIAL PRIMARY KEY,
    player_black_id    INT REFERENCES Users (id),
    player_white_id    INT REFERENCES Users (id),
    game_id    INT REFERENCES Game (id),
    created_at DATE NOT NULL
);

