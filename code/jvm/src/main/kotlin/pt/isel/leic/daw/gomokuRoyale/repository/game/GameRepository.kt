package pt.isel.leic.daw.gomokuRoyale.repository.game

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Game

class GameRepository(private val handle: Handle) : GameRepositoryInterface {
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
            cu.id as creator_user_id, cu.username as creator_user_username, cu.email as creator_user_email, 
            cu.password as creator_user_password, cu.rating as creator_user_rating, cu.nr_games_played as creator_user_nr_games_played,
            ju.id as join_user_id, ju.username as join_user_username, ju.email as join_user_email, 
            ju.password as join_user_password, ju.rating as join_user_rating, ju.nr_games_played as join_user_nr_games_played
        FROM games g 
        LEFT JOIN lobbys l on l.id = g.lobby_id
        LEFT JOIN users cu ON l.creator_user_id = cu.id
        LEFT JOIN users ju ON l.join_user_id = ju.id
        WHERE l.id = :id
    """
        )
            .bind("id", gameId)
            .mapTo<Game>()
            .firstOrNull()
    }

    override fun updateGameWinner(gameId: Int, winner: Int): Int =
        handle.createUpdate(
            """
            update games set winner =:winner, state = 'FINISHED' where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("winner", winner)
            .execute()

    override fun updateGameBoard(gameId: Int, turn: Int, board: String): Int {
        return handle.createUpdate(
            """
            update games set board = CAST(:board AS jsonb), turn = :turn where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("turn", turn)
            .bind("board", board)
            .execute()
    }


    override fun getGameByLobbyId(lobbyId: Int): Game? =
        handle.createQuery("select * from games where lobby_id = :lobbyId")
            .bind("lobbyId", lobbyId)
            .mapTo<Game>()
            .firstOrNull()
}
