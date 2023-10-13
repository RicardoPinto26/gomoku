package pt.isel.leic.daw.gomokuRoyale.repository.game

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Board
import pt.isel.leic.daw.gomokuRoyale.domain.Game

class GameStateRepository(private val handle: Handle) : GameStateRepositoryInterface {

    override fun createGameState(gameId: Int, turn: Int): Int =
        handle.createUpdate(
            """
                insert into game_state (game_id, turn) values (:game_id, :turn)
                """
        )
            .bind("game_id", gameId)
            .bind("turn", turn)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

    override fun getGameStateByGameId(gameId: Int): Game? =
        handle.createQuery("select * from game_state where game_id = :game_id")
            .bind("game_id", gameId)
            .mapTo<Game>()
            .firstOrNull()

    override fun updateGameState(gameId: Int, board: Board): Int =
        handle.createUpdate(
            """
            update game_state set board = :board where game_id = :game_id
        """
        ).bind("game_id", gameId)
            .bind("board", board.internalBoard.toString()) // TODO: change this to something better
            .execute()

    override fun updateGameStateWinner(gameId: Int, winner: Int): Int =
        handle.createUpdate("""
            update game_state set winner =:winner where game_id = :game_id
        """.trimIndent())
            .bind("winner", winner)
            .execute()

    override fun forfeitGame(gameId: Int, user: Int): Int {
        TODO("Not yet implemented")
    }

    override fun deleteGameState(gameId: Int): Int =
        handle.createUpdate("delete from game_state where game_id = :game_id")
            .bind("game_id", gameId)
            .execute()
}