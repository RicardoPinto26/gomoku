package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.GameSettings
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import java.sql.Date
import java.sql.ResultSet

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

        return Lobby(
            name,
            id,
            null,
            user1,
            user2,
            pointsMargin,
            Date.from(createdAt),
            GameSettings(gridSize, winningLenght, opening, overflow)
        )
    }
}
