package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import java.sql.Date
import java.sql.ResultSet
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.BlackPlayer
import pt.isel.leic.daw.gomokuRoyale.domain.BoardDraw
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.GameSettings
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.WhitePlayer
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInGame
import pt.isel.leic.daw.gomokuRoyale.domain.parseJsonToBoard

class LobbyMapper : RowMapper<Lobby> {
    companion object {
        private val logger = LoggerFactory.getLogger(LobbyMapper::class.java)
    }

    override fun map(rs: ResultSet, ctx: StatementContext): Lobby {
        logger.info("Started Mapping lobby")
        logger.info("mapping name")
        val name = rs.getString("name")
        logger.info("mapping id")
        val id = rs.getInt("id")
        logger.info("mapping join_user_id")
        val joinUserId = rs.getInt("join_user_id")
        logger.info("mapping grid_size")
        val gridSize = rs.getInt("grid_size")
        logger.info("mapping opening")
        val opening = Opening.from(rs.getString("opening")) ?: throw Exception("Invalid opening")
        val winningLenght = rs.getInt("winning_lenght")
        val overflow = rs.getBoolean("overflow")
        val pointsMargin = rs.getInt("points_margin")
        val createdAt = rs.getTimestamp("created_at").toInstant()

        val user1 = UserMapper().map(rs, "user1_")
        val user2 = if (joinUserId > 0) UserMapper().map(rs, "user2_") else null

        logger.info("LobbyMapper: $id, $joinUserId, $gridSize, $pointsMargin, $createdAt")
        if(user2 != null) {
            val gameIndex = rs.getInt("game_index")
            val gameState = rs.getString("game_state")
            val winner = rs.getInt("game_winner")
            val board = rs.getObject("game_board").toString().parseJsonToBoard()
            val draw = winner == 0 && gameState == "FINISHED"
            val openingVariant = rs.getString("game_opening_variant")
            val blackPlayer = rs.getInt("game_black_player")
            val whitePlayer = rs.getInt("game_white_player")
            val turn = when (rs.getInt("game_turn")) {
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

                else ->
                    throw UserNotInGame("User not in game")
            }

            val settings = GameSettings(
                gridSize,
                winningLenght,
                if (openingVariant == null) opening else Opening.valueOf(openingVariant),
                overflow
            )


            return Lobby(
                name,
                id,
                game = Game(
                    name,
                    user1,
                    user2,
                    settings,
                    gameIndex,
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
                ),
                user1,
                user2,
                pointsMargin,
                Date.from(createdAt),
                settings
            )
        } else {
            val settings = GameSettings(
                gridSize,
                winningLenght,
                opening,
                overflow
            )

            return Lobby(
                name,
                id,
                null,
                user1,
                null,
                pointsMargin,
                Date.from(createdAt),
                settings
            )
        }
    }
}
