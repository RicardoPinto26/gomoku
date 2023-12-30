drop table if EXISTS users cascade;
drop table if EXISTS tokens cascade;
drop table if EXISTS games cascade;
drop table if EXISTS lobbys cascade;
drop table if EXISTS game_State cascade;
drop table if EXISTS moves cascade;

create table users
(
    id              SERIAL PRIMARY KEY,
    username        VARCHAR(50)      NOT NULL UNIQUE,
    email           varchar(255)     NOT NULL CHECK (email SIMILAR TO '_%@_%') UNIQUE,
    password        CHAR(64)         NOT NULL CHECK ( char_length(password) = 64 ),
    rating          DOUBLE PRECISION NOT NULL CHECK (rating >= 0)          DEFAULT 800.0,
    nr_games_played INT              NOT NULL CHECK (nr_games_played >= 0) DEFAULT 0
);

create table tokens
(
    token        VARCHAR(256) primary key,
    user_id      INT REFERENCES users (id),
    created_at   BIGINT NOT NULL,
    last_used_at BIGINT NOT NULL
);

create table lobbys
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    creator_user_id INT         NOT NULL REFERENCES users (id),
    join_user_id    INT REFERENCES users (id) DEFAULT NULL,
    grid_size       INT         NOT NULL,
    opening         VARCHAR(50) NOT NULL CHECK ( opening IN
                                                 ('PRO', 'LONG PRO', 'SWAP', 'SWAP2', 'LONG_PRO', 'FREESTYLE')),
    winning_lenght  INT         NOT NULL      DEFAULT 5,
    overflow        BOOLEAN     NOT NULL      DEFAULT FALSE,
    points_margin   INT         NOT NULL      DEFAULT 200,
    created_at      timestamp   NOT NULL      DEFAULT current_timestamp
);

create table games
(
    id              SERIAL PRIMARY KEY,
    lobby_id        INT REFERENCES lobbys (id) NOT NULL,
    turn            INT REFERENCES users (id)  NOT NULL,
    black_player    INT REFERENCES users (id)  NOT NULL,
    white_player    INT REFERENCES users (id)  NOT NULL,
    winner          INT REFERENCES users (id)                                                        DEFAULT NULL,
    board           jsonb                      NOT NULL,
    opening_index   INT                        NOT NULL                                              DEFAULT 0,
    opening_variant VARCHAR(50) CHECK (opening_variant IN ('SWAP2_1', 'SWAP2_2', 'SWAP2_3'))         DEFAULT NULL,
    state           VARCHAR(30) CHECK ( state IN ('AWAITING FIRST MOVE', 'IN_PROGRESS', 'FINISHED')) DEFAULT 'AWAITING FIRST MOVE'
);

create table moves
(
    id      SERIAL PRIMARY KEY,
    game_id INT REFERENCES games (id) NOT NULL,
    user_id INT REFERENCES users (id) NOT NULL,
    move_x  INT                       NOT NULL,
    move_y  INT                       NOT NULL
    --move_time  TIMESTAMP                 NOT NULL
);


INSERT INTO users (username, email, password)
VALUES ('Chiquinho do ka100', 'a46631@alunos.isel.pt', '3e23edee0e2d5b04162bcbf6b34257890435f595e623e5fbc6b99f286f155e3a'),
       ('Luis Macario', 'a47671@alunos.isel.pt', '5cd04b9b8b15fe69460218d1ed0f5724cfb6f7c8ef4c3b3e3c25f89c435b391c'),
       ('Ricardo Pinto', 'a47673@alunos.isel.pt', 'f7d3ce0d2bc3127c7c898e35c6d8b59f3faaf2243cb6e8f6c960c3a33c055d6b'),
       ('uilde', 'a47674@alunos.isel.pt', '3CB68DD8312A4549CA4F4A044510A2AC88AB95F988202C0A52400C5866152885'),
       ('uilde1', 'a47675@alunos.isel.pt', '3CB68DD8312A4549CA4F4A044510A2AC88AB95F988202C0A52400C5866152885');

INSERT INTO tokens (token, user_id, created_at, last_used_at)
VALUES ('17NPPxEeGEUZl6wnkPrHMQNyC80NvequkDNEb6B872o=', 1, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW())),
       ('27NPPxEeGEUZl6wnkPrHMQNyC80NvequkDNEb6B872o=', 2, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW())),
       ('37NPPxEeGEUZl6wnkPrHMQNyC80NvequkDNEb6B872o=', 3, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()));

INSERT INTO lobbys (name, creator_user_id, join_user_id, grid_size, opening)
VALUES ('Game SWAP2', 1, 2, 15, 'SWAP2'),
       ('Game PRO', 2, 1, 19, 'PRO'),
       ('Game SWAP', 1, 2, 15, 'SWAP'),
       ('Game 19', 1, null, 15, 'FREESTYLE'),
       ('Game 17', 1, null, 15, 'FREESTYLE'),
       ('Game 116', 1, null, 15, 'FREESTYLE'),
       ('Game 14', 1, null, 15, 'FREESTYLE'),
       ('Game 9', 1, null, 10, 'FREESTYLE'),
       ('Game 10', 1, null, 10, 'FREESTYLE');


INSERT INTO games (lobby_id, turn, winner,black_player, white_player, board, state)
VALUES (1, 1, null, 1,2, '[[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'AWAITING FIRST MOVE'),
       (3, 1, null, 1, 2, '[[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'AWAITING FIRST MOVE'),
       (2, 1, null, 2,1 ,'[[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'AWAITING FIRST MOVE'),
       (1, 1, null, 1, 2,'[["WHITE","BLACK"], ["WHITE",null]]', 'IN_PROGRESS'),
       (1, 2, null, 1, 2, '[["WHITE","WHITE","WHITE","WHITE", null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'IN_PROGRESS');

INSERT INTO moves (game_id, user_id, move_x, move_y)
VALUES (1, 1, 7, 7),
       (1, 2, 8, 8),
       (2, 2, 9, 9);

