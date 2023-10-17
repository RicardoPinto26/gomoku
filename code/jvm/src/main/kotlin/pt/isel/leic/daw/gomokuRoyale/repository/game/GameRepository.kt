package pt.isel.leic.daw.gomokuRoyale.repository.game

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Game

class GameRepository(private val handle: Handle) : GameRepositoryInterface {
    override fun createGame(name: String, lobbyId: Int, playerBlack: Int, playerWhite: Int): Int =
        handle.createUpdate(
            """
            INSERT INTO games (name, lobby_id, player_black_id, player_white_id) 
            VALUES (:name, :lobbyId, :playerBlack, :playerWhite)
        """
        )
            .bind("name", name)
            .bind("lobbyId", lobbyId)
            .bind("playerBlack", playerBlack)
            .bind("playerWhite", playerWhite)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

    override fun getGameById(gameId: Int): Game? =
        handle.createQuery("select * from games where id = :gameId")
            .bind("gameId", gameId)
            .mapTo<Game>()
            .firstOrNull()

    override fun getGameByLobbyId(lobbyId: Int): Game? =
        handle.createQuery("select * from games where lobby_id = :lobbyId")
            .bind("lobbyId", lobbyId)
            .mapTo<Game>()
            .firstOrNull()

    override fun getGameByPlayerIdAndLobbyId(playerId: Int, lobbyId: Int): Game? =
        handle.createQuery("select * from games where (player_black_id = :playerId or player_white_id = :playerId) and lobby_id = :lobbyId")
            .bind("playerId", playerId)
            .bind("lobbyId", lobbyId)
            .mapTo<Game>()
            .firstOrNull()
}
