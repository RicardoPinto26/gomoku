package pt.isel.leic.daw.gomokuRoyale.repository.lobby

import org.apache.catalina.User
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

class LobbyRepository(private val handle: Handle) : LobbyRepositoryInterface {

    override fun createLobby(userId: Int, gridSize: Int, opening: String, variant: String, pointsMargin: Int): Int =
        handle.createUpdate(
            """
                insert into lobbys(creator_user_id, grid_size, opening, variant, points_margin)
                values(:creatorId, :gridSize, :opening, :variant, :pointsMargin)
                """
        )
            .bind("creatorId", userId)
            .bind("gridSize", userId)
            .bind("opening", opening)
            .bind("variant", variant)
            .bind("pointsMargin", pointsMargin)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

    override fun joinLobby(userId: Int, lobbyId: Int): Int =
        handle.createUpdate(
            """
                update lobbys
                set join_user_id = :userId
                where id = :lobbyId
                """
        )
            .bind("userId", userId)
            .bind("lobbyId", lobbyId)
            .execute()

    override fun getLobbyById(lobbyId: Int): Lobby? =
        handle.createQuery("select * from lobbys where id = :id")
            .bind("id", lobbyId)
            .mapTo<Lobby>()
            .firstOrNull()

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
