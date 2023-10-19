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
        val name = rs.getString("name")
        val id = rs.getInt("id")
        val joinUserId = rs.getInt("join_user_id")
        val gridSize = rs.getInt("grid_size")
        val pointsMargin = rs.getInt("points_margin")
        val createdAt = rs.getTimestamp("created_at").toInstant()

        val user1 = UserMapper().map(rs, "user1_")
        val user2 = if (joinUserId > 0) UserMapper().map(rs, "user2_") else null

        logger.info("LobbyMapper: $id, $joinUserId, $gridSize, $pointsMargin, $createdAt, $user1, $user2")

        return Lobby(
                name,
                id,
                null,
                user1,
                user2,
                pointsMargin,
                Date.from(createdAt),
                GameSettings(gridSize, 5, Opening.FREESTYLE)
        )
    }
}
