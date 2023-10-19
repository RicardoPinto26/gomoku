drop table if EXISTS users cascade;
drop table if EXISTS tokens cascade;
drop table if EXISTS games cascade;
drop table if EXISTS lobbys cascade;
drop table if EXISTS game_State cascade;
drop table if EXISTS moves cascade;

create table users
(
    id              SERIAL PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    email           varchar(255) NOT NULL CHECK (email SIMILAR TO '_%@_%') UNIQUE,
    password        CHAR(64)     NOT NULL CHECK ( char_length(password) = 64 ),
    rating          DOUBLE PRECISION  NOT NULL CHECK (rating >= 0)  DEFAULT 800.0,
    nr_games_played INT          NOT NULL CHECK (nr_games_played >= 0) DEFAULT 0
);

create table tokens
(
    token        VARCHAR(256) primary key,
    user_id      INT REFERENCES users (id),
    created_at   BIGINT NOT NULL ,
    last_used_at BIGINT NOT NULL
);

create table lobbys
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    creator_user_id INT         NOT NULL REFERENCES users (id),
    join_user_id    INT         REFERENCES users (id) DEFAULT NULL,
    grid_size       INT         NOT NULL,
    opening         VARCHAR(50) NOT NULL,
    variant         VARCHAR(50) NOT NULL,
    points_margin   INT         NOT NULL DEFAULT 200,
    created_at      timestamp   NOT NULL DEFAULT  current_timestamp
);

create table games
(
    id          SERIAL PRIMARY KEY,
    lobby_id    INT REFERENCES lobbys (id) NOT NULL,
    turn        INT REFERENCES users (id) NOT NULL,
    winner      INT REFERENCES users (id) DEFAULT NULL,
    board       jsonb NOT NULL,
    state       VARCHAR(30) CHECK ( state IN ('AWAITING FIRST MOVE', 'IN_PROGRESS', 'FINISHED')) DEFAULT 'AWAITING FIRST MOVE'
);

create table moves
(
    id      SERIAL PRIMARY KEY,
    game_id INT REFERENCES games (id) NOT NULL,
    user_id INT REFERENCES users (id) NOT NULL,
    move_x  INT                       NOT NULL,
    move_y  INT                       NOT NULL
    --move_time  TIMESTAMP                 NOT NULL
)

