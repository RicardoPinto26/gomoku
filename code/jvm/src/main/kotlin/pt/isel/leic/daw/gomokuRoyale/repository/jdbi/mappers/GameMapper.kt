package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.BlackPlayer
import pt.isel.leic.daw.gomokuRoyale.domain.BoardDraw
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.GameSettings
import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.WhitePlayer
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInGame
import pt.isel.leic.daw.gomokuRoyale.domain.parseJsonToBoard
import java.sql.ResultSet

class GameMapper : RowMapper<Game> {
    companion object {
        private val logger = LoggerFactory.getLogger(GameMapper::class.java)
    }

    override fun map(rs: ResultSet, ctx: StatementContext): Game? {
        logger.info("Started Mapping game")

        logger.info("Mapping game_name")
        val name = rs.getString("game_name")
        logger.info("Mapping lobby")
        val lobby = LobbyMapper().map(rs, ctx)
        val user1 = lobby.user1
        val user2 = lobby.user2 ?: return null
        logger.info("mapping opening variant")
        val openingVariant = rs.getString("game_opening_variant")
        val settings = GameSettings(
            lobby.settings.boardSize,
            lobby.settings.winningLength,
            if (openingVariant == null) lobby.settings.opening else Opening.valueOf(openingVariant),
            lobby.settings.overflowAllowed
        )
        logger.info("mapping gameboard")
        val boardJson = rs.getObject("game_board").toString()
        val currentOpeningIndex = rs.getInt("game_opening_index")
        val gameState = rs.getString("game_state")
        val blackPlayer = rs.getInt("game_black_player")
        val whitePlayer = rs.getInt("game_white_player")
        val winner = rs.getInt("game_winner")
        val draw = winner == 0 && gameState == "FINISHED"
        val board = boardJson.parseJsonToBoard()
        val turnID = rs.getInt("game_turn")
        val turn = when (turnID) {
            user1.id -> {
                if (blackPlayer == user1.id) {
                    BlackPlayer(user1)
                } else {
                    WhitePlayer(user1)
                }
            }

            user2.id -> {
                if (whitePlayer == user2.id) {
                    WhitePlayer(user2)
                } else {
                    BlackPlayer(user2)
                }
            }

            else -> {
                throw UserNotInGame("User not in game")
            }
        }
        if (turnID == user1.id) {
            BlackPlayer(user1)
        } else {
            WhitePlayer(user2)
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
                    BlackPlayer(if (blackPlayer == user1.id) user1 else user2),
                    WhitePlayer(if (whitePlayer == user2.id) user2 else user1),
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
