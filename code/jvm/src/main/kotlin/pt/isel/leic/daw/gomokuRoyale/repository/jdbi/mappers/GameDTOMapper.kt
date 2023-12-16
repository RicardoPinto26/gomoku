package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.user.GameDTO
import java.sql.ResultSet

class GameDTOMapper : RowMapper<GameDTO> {
    companion object {
        private val logger = LoggerFactory.getLogger(GameDTOMapper::class.java)
    }

    override fun map(rs: ResultSet, ctx: StatementContext?): GameDTO {
        // Game Info
        logger.info("mapping gameDTO info")
        val id = rs.getInt("id")
        val lobbyId = rs.getInt("lobby_id")
        val boardJson = rs.getObject("board").toString()
        val currentOpeningIndex = rs.getInt("opening_index")
        val gameState = rs.getString("state")
        val blackPlayer = rs.getInt("black_player")
        val whitePlayer = rs.getInt("white_player")
        val winner = rs.getInt("winner")
        val turn = rs.getInt("turn")
        val openingVariant = rs.getString("opening_variant")
        // logger.info("mapping game info done $id $lobbyId $boardJson $currentOpeningIndex $gameState $blackPlayer $whitePlayer $winner $turn $openingVariant")

        return GameDTO(
            id,
            lobbyId,
            turn,
            blackPlayer,
            whitePlayer,
            winner,
            currentOpeningIndex,
            openingVariant,
            gameState,
            boardJson
        )
    }
}
