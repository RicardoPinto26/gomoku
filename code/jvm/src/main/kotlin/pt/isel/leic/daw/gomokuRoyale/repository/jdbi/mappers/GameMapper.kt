package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.*
import java.sql.ResultSet

class GameMapper : RowMapper<Game> {
    companion object {
        private val logger = LoggerFactory.getLogger(GameMapper::class.java)
    }

    override fun map(rs: ResultSet, ctx: StatementContext): Game? {
        val lobby = LobbyMapper().map(rs, ctx)
        val name = rs.getString("game_name")
        val user1 = lobby.user1
        val user2 = lobby.user2 ?: return null
        val settings = GameSettings(
            lobby.settings.boardSize,
            lobby.settings.winningLength,
            lobby.settings.opening,
            lobby.settings.overflowAllowed
        )
        val boardJson = rs.getObject("game_board").toString()
        val currentOpeningIndex = rs.getInt("game_opening_index")
        val gameState = rs.getString("game_state")
        val winner = rs.getInt("game_winner")
        val draw = winner == 0 && gameState == "FINISHED"
        val board = boardJson.parseJsonToBoard()
        val turnID = rs.getInt("game_turn")
        val turn =
            if (turnID == user1.id) {
                createBlackPlayer(user1)
            } else {
                createWhitePlayer(user2)
            }

        val game = Game(
            name,
            user1,
            user2,
            settings,
            currentOpeningIndex,
            if (winner == 0 && !draw) {
                BoardRun(
                    settings.winningLength,
                    settings.overflowAllowed,
                    BlackPlayer(user1),
                    WhitePlayer(user2),
                    turn,
                    board
                )
            } else if (draw) {
                BoardDraw(board)
            } else {
                BoardWin(board, if (winner == user1.id) BlackPlayer(user1) else WhitePlayer(user2))
            }
        )

        return game
    }
}
