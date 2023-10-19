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
                user1.id as user1_id, user1.username as user1_username, user1.email as user1_email,
                user1.password as user1_password, user1.rating as user1_rating, user1.nr_games_played as user1_nr_games_played,
                user2.id as user2_id, user2.username as user2_username, user2.email as user2_email,
                user2.password as user2_password, user2.rating as user2_rating, user2.nr_games_played as user2_nr_games_played
            FROM lobbys l
            LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
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
                player1.id as creator_user_id, player1.username as creator_user_username, player1.email creator_user_email,
                player1.password as creator_user_password, player1.rating as creator_user_rating, player1.nr_games_played creator_user_nr_games_played,
                player2.id as join_user_id, player2.username as join_user_username, player2.email creator_user_email,
                player2.password as creator_user_password, player2.rating as creator_user_rating, player2.nr_games_played creator_user_nr_games_played
            from lobbys as l
            LEFT JOIN users as player1 ON l.creator_user_id = player1.id
            LEFT JOIN users as player2 ON l.join_user_id = player2.id
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
