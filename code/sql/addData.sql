INSERT INTO users (username, email, password)
VALUES ('Chiquinho do ka100', 'a46631@alunos.isel.pt', '3e23edee0e2d5b04162bcbf6b34257890435f595e623e5fbc6b99f286f155e3a'),
       ('Luis Macario', 'a47671@alunos.isel.pt', '5cd04b9b8b15fe69460218d1ed0f5724cfb6f7c8ef4c3b3e3c25f89c435b391c'),
       ('Ricardo Pinto', 'a47673@alunos.isel.pt', 'f7d3ce0d2bc3127c7c898e35c6d8b59f3faaf2243cb6e8f6c960c3a33c055d6b');

INSERT INTO tokens (token, user_id, created_at, last_used_at)
VALUES ('17NPPxEeGEUZl6wnkPrHMQNyC80NvequkDNEb6B872o=', 1, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW())),
       ('27NPPxEeGEUZl6wnkPrHMQNyC80NvequkDNEb6B872o=', 2, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW())),
       ('37NPPxEeGEUZl6wnkPrHMQNyC80NvequkDNEb6B872o=', 3, EXTRACT(EPOCH FROM NOW()), EXTRACT(EPOCH FROM NOW()));

INSERT INTO lobbys (name, creator_user_id, join_user_id, grid_size, opening, variant)
VALUES ('Game 1', 1, 2, 15, 'Standard', 'Classic'),
       ('Game 2', 2, 1, 19, 'Standard', 'Classic');

INSERT INTO games (lobby_id, turn, winner, board, state)
VALUES (1, 1, null, '[[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'AWAITING FIRST MOVE'),
       (2, 1, null, '[[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'AWAITING FIRST MOVE'),
       (1, 1, null, '[["WHITE","BLACK"], ["WHITE",null]]', 'IN_PROGRESS'),
       (1, 2, null, '[["WHITE","WHITE","WHITE","WHITE", null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null],[null,null,null,null,null]]', 'IN_PROGRESS');

INSERT INTO moves (game_id, user_id, move_x, move_y)
VALUES (1, 1, 7, 7),
       (1, 2, 8, 8),
       (2, 2, 9, 9);

