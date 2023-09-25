package pt.isel.leic.daw.GomokuRoyale.repository.lobby

import org.apache.catalina.User
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.GomokuRoyale.domain.Lobby
import pt.isel.leic.daw.GomokuRoyale.domain.Opening

class JdbiLobbyRepository(private val handle: Handle) : JdbiLobbyInterface {

    override fun createLobby(userId: Int, gridSize: Int, opening: Opening, variant: String, pointsMargin: Int): Int =
        handle.createUpdate(
            """
                insert into lobbys(creator_user_id, grid_size, opening, variant, points_margin)
                values(:creatorId, :gridSize, :opening, :variant, :pointsMargin)
                """
        )
            .bind("creatorId", userId)
            .bind("gridSize", userId)
            .bind("opening", opening.name)
            .bind("variant", variant)
            .bind("pointsMargin", pointsMargin)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()


    override fun getUserLobbys(userId: Int): List<User> =
        handle.createQuery("select * from lobbys where user_id = :user_id")
            .bind("user_id", userId)
            .mapTo<User>()
            .toList()


    override fun getLobbyByOpening(opening: String): Lobby? =
        handle.createQuery("select * from lobbys where opening = :opening")
            .bind("opening", opening)
            .mapTo<Lobby>()
            .firstOrNull()


    override fun getLobbyByVariant(variant: String): Lobby? =
        handle.createQuery("select * from lobbys where variant = :variant")
            .bind("variant", variant)
            .mapTo<Lobby>()
            .firstOrNull()
}