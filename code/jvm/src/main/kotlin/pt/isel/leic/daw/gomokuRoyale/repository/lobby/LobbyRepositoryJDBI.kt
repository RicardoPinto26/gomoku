package pt.isel.leic.daw.gomokuRoyale.repository.lobby

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

class LobbyRepositoryJDBI(private val handle: Handle) : LobbyRepository {

    override fun createLobby(
        name: String,
        userId: Int,
        gridSize: Int,
        opening: String,
        variant: String,
        pointsMargin: Int
    ): Int =
        handle.createUpdate(
            """
                insert into lobbys(name, creator_user_id, grid_size, opening, variant, points_margin)
                values(:name, :creatorId, :gridSize, :opening, :variant, :pointsMargin)
                """
        )
            .bind("name", name)
            .bind("creatorId", userId)
            .bind("gridSize", gridSize)
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
        handle.createQuery(
            """
        SELECT 
            l.*,
            cu.id as creator_user_id, cu.username as creator_user_username, cu.email as creator_user_email, 
            cu.password as creator_user_password, cu.rating as creator_user_rating, cu.nr_games_played as creator_user_nr_games_played,
            ju.id as join_user_id, ju.username as join_user_username, ju.email as join_user_email, 
            ju.password as join_user_password, ju.rating as join_user_rating, ju.nr_games_played as join_user_nr_games_played
        FROM lobbys l
        LEFT JOIN users cu ON l.creator_user_id = cu.id
        LEFT JOIN users ju ON l.join_user_id = ju.id
        WHERE l.id = :id
    """
        )
            .bind("id", lobbyId)
            .mapTo<Lobby>()
            .firstOrNull()

    override fun getUserLobbys(userId: Int): List<Lobby> =
        handle.createQuery(
            """
            select 
            l.*,
            cu.id as creator_user_id, cu.username as creator_user_username, cu.email as creator_user_email, 
            cu.password as creator_user_password, cu.rating as creator_user_rating, cu.nr_games_played as creator_user_nr_games_played,
            ju.id as join_user_id, ju.username as join_user_username, ju.email as join_user_email, 
            ju.password as join_user_password, ju.rating as join_user_rating, ju.nr_games_played as join_user_nr_games_played
            from lobbys as l
            LEFT JOIN users cu ON l.creator_user_id = cu.id
            LEFT JOIN users ju ON l.join_user_id = ju.id
            where l.creator_user_id = :user_id OR l.join_user_id = :user_id
            """.trimIndent()
        )
            .bind("user_id", userId)
            .mapTo<Lobby>()
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

    override fun seekLobbyID(
        otherRating: Int,
        gridSize: Int,
        winningLength: Int,
        opening: String,
        minRating: Int,
        maxRating: Int
    ): Int? =
        handle.createQuery(
            """
            select l.id
            from lobbys as l
            join users as u on l.creator_user_id = u.id
            where l.join_user_id is null and l.grid_size = :grid_size and l.opening = :opening
            AND u.rating between :min_rating and :max_rating
            """.trimIndent()
        )
            .bind("grid_size", gridSize)
            // .bind("winning_length", winningLength)
            .bind("opening", opening)
            .bind("min_rating", minRating)
            .bind("max_rating", maxRating)
            .mapTo<Int>()
            .firstOrNull()
}
