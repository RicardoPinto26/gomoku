package pt.isel.leic.daw.gomokuRoyale.repository.game

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Game

class GameRepositoryJDBI(private val handle: Handle) : GameRepository {
    override fun createGame(lobbyId: Int, turn: Int, board: String): Int =
        handle.createUpdate(
            """
            INSERT INTO games (lobby_id, turn, board) 
            VALUES (:lobbyId, :turn, CAST(:board AS jsonb))
        """
        )
            .bind("lobbyId", lobbyId)
            .bind("turn", turn)
            .bind("board", board)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

    override fun getGameById(gameId: Int): Game? {
        return handle.createQuery(
            """
        SELECT 
            l.name as game_name, g.turn as game_turn, g.board as game_board, g.winner as game_winner,
            l.*,
             user1.id as user1_id, user1.username as user1_username, user1.email as user1_email,
                user1.password as user1_password, user1.rating as user1_rating, user1.nr_games_played as user1_nr_games_played,
                user2.id as user2_id, user2.username as user2_username, user2.email as user2_email,
                user2.password as user2_password, user2.rating as user2_rating, user2.nr_games_played as user2_nr_games_played
        FROM games g 
        LEFT JOIN lobbys l on l.id = g.lobby_id
        LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
        WHERE g.id = :id
    """
        )
            .bind("id", gameId)
            .mapTo<Game>()
            .firstOrNull()
    }

    override fun updateGameWinner(gameId: Int, winner: Int, board: String): Int =
        handle.createUpdate(
            """
            update games set board = CAST(:board AS jsonb), winner =:winner, state = 'FINISHED' where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("board", board)
            .bind("winner", winner)
            .execute()

    override fun updateGameBoard(gameId: Int, turn: Int, board: String): Int {
        return handle.createUpdate(
            """
            update games set board = CAST(:board AS jsonb), turn = :turn, state = 'IN_PROGRESS' where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("turn", turn)
            .bind("board", board)
            .execute()
    }

    override fun updateGameDraw(gameId: Int, board: String): Int =
        handle.createUpdate(
            """
            update games set board = CAST(:board AS jsonb), state = 'FINISHED' where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("board", board)
            .execute()

    override fun getGameByLobbyId(lobbyId: Int): Game? =
        handle.createQuery("select * from games where lobby_id = :lobbyId")
            .bind("lobbyId", lobbyId)
            .mapTo<Game>()
            .firstOrNull()
}
