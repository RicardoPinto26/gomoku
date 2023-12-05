package pt.isel.leic.daw.gomokuRoyale.repository.game

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Game

class GameRepositoryJDBI(private val handle: Handle) : GameRepository {
    override fun createGame(lobbyId: Int, turn: Int, blackPlayer: Int, whitePlayer: Int, board: String): Int =
        handle.createUpdate(
            """
            INSERT INTO games (lobby_id, turn, black_player, white_player, board) 
            VALUES (:lobbyId, :turn, :blackPlayer, :whitePlayer, CAST(:board AS jsonb))
        """
        )
            .bind("lobbyId", lobbyId)
            .bind("turn", turn)
            .bind("blackPlayer", blackPlayer)
            .bind("whitePlayer", whitePlayer)
            .bind("board", board)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

    override fun getGameById(gameId: Int): Game? {
        return handle.createQuery(
            """
        SELECT 
            l.name as game_name, g.turn as game_turn, g.board as game_board, g.winner as game_winner,
             g.opening_index as game_opening_index, g.state as game_state,
              g.black_player as game_black_player, g.white_player as game_white_player, g.opening_variant as game_opening_variant,
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

    override fun updateGamePlayer(gameId: Int, newPlayer1: Int, newPlayer2: Int, turn: Int, newOpeningIndex: Int): Int =
        handle.createUpdate(
            """
            update games set black_player = :newPlayer1, white_player = :newPlayer2,
             turn = :turn, opening_index = :newOpeningIndex
             where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("newPlayer1", newPlayer1)
            .bind("newPlayer2", newPlayer2)
            .bind("turn", turn)
            .bind("newOpeningIndex", newOpeningIndex)
            .execute()

    override fun updateGameBoard(gameId: Int, turn: Int, board: String, openingIndex: Int): Int {
        return handle.createUpdate(
            """
            update games set board = CAST(:board AS jsonb),opening_index = :index, turn = :turn, state = 'IN_PROGRESS' where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("index", openingIndex)
            .bind("turn", turn)
            .bind("board", board)
            .execute()
    }

    override fun updateOpeningIndex(gameId: Int, openingIndex: Int): Int {
        return handle.createUpdate(
            """
            update games set opening_index = :opening_index where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("opening_index", openingIndex)
            .execute()
    }

    override fun updateOpeningVariant(gameId: Int, openingVariant: String, openingIndex: Int): Int {
        return handle.createUpdate(
            """
            update games set opening_variant = :opening_variant,  opening_index = :opening_index 
            where id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .bind("opening_variant", openingVariant)
            .bind("opening_index", openingIndex)
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
