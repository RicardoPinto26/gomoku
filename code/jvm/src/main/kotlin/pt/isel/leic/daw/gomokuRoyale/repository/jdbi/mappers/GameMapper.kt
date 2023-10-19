package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.BlackPlayer
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.GameSettings
import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.createBlackPlayer
import pt.isel.leic.daw.gomokuRoyale.domain.createWhitePlayer
import pt.isel.leic.daw.gomokuRoyale.domain.parseJsonToBoard
import java.sql.ResultSet

class GameMapper : RowMapper<Game> {
    companion object{
        private val logger = LoggerFactory.getLogger(GameMapper::class.java)
    }
    override fun map(rs: ResultSet, ctx: StatementContext): Game? {
        val lobby = LobbyMapper().map(rs, ctx)
        val name = rs.getString("game_name")
        val user1 = lobby.user1
        val user2 = lobby.user2 ?: return null
        val settings = GameSettings(lobby.settings.boardSize, 5, Opening.FREESTYLE)
        val boardJson = rs.getObject("game_board").toString()
        val winner = rs.getInt("game_winner")
        val board = boardJson.parseJsonToBoard()
        logger.info(board.toString())
        val turn =
            if (rs.getInt("game_turn") == 1) {
                createBlackPlayer(user1)
            } else {
                createWhitePlayer(user2)
            }

        return Game(
            name,
            user1,
            user2,
            settings,
            if (winner == 0) {
                BoardRun(
                    // settings.boardSize,
                    settings.winningLength,
                    settings.overflowAllowed,
                    turn,
                    BlackPlayer(user1),
                    BlackPlayer(user2),
                    board
                )
            } else {
                BoardWin(board, BlackPlayer(user1))
            }
        )
    }
}
