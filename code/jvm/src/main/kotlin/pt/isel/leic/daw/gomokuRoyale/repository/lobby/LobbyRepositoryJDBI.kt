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
        winningLenght: Int,
        pointsMargin: Int,
        overflow: Boolean
    ): Int =
        handle.createUpdate(
            """
                insert into lobbys(name, creator_user_id, grid_size, opening, winning_lenght, overflow, points_margin)
                values(:name, :creatorId, :gridSize, :opening, :winningLenght, :overflow, :pointsMargin)
                """
        )
            .bind("name", name)
            .bind("creatorId", userId)
            .bind("gridSize", gridSize)
            .bind("opening", opening)
            .bind("winningLenght", winningLenght)
            .bind("overflow", overflow)
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
                l.name as game_name, g.opening_index as game_index, g.board as game_board, g.turn as game_turn, g.winner as game_winner, g.state as game_state,
                g.id as game_id, g.black_player as game_black_player, g.white_player as game_white_player, g.opening_variant as game_opening_variant,
                l.*,
                user1.id as user1_id, user1.username as user1_username, user1.email as user1_email,
                user1.password as user1_password, user1.rating as user1_rating, user1.nr_games_played as user1_nr_games_played,
                user2.id as user2_id, user2.username as user2_username, user2.email as user2_email,
                user2.password as user2_password, user2.rating as user2_rating, user2.nr_games_played as user2_nr_games_played
            FROM lobbys l
            LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
            LEFT JOIN games g on l.id = g.lobby_id
            WHERE l.id = :id
            """
        )
            .bind("id", lobbyId)
            .mapTo<Lobby>()
            .firstOrNull()

    override fun getUserLobbys(userId: Int): List<Lobby> =
        handle.createQuery(
            """
            SELECT 
                l.name as game_name, g.opening_index as game_index, g.board as game_board, g.turn as game_turn, g.winner as game_winner, g.state as game_state,
                g.id as game_id, g.black_player as game_black_player, g.white_player as game_white_player, g.opening_variant as game_opening_variant,
                l.*,
                user1.id as user1_id, user1.username as user1_username, user1.email as user1_email,
                user1.password as user1_password, user1.rating as user1_rating, user1.nr_games_played as user1_nr_games_played,
                user2.id as user2_id, user2.username as user2_username, user2.email as user2_email,
                user2.password as user2_password, user2.rating as user2_rating, user2.nr_games_played as user2_nr_games_played
            FROM lobbys l
            LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
            LEFT JOIN games g on l.id = g.lobby_id
                   where (l.creator_user_id = :user_id OR l.join_user_id = :user_id) and g.state <> 'FINISHED'
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

    override fun getLobbyByGameId(gameId: Int): Lobby? =
        handle.createQuery(
            """
                SELECT 
                l.name as game_name, g.opening_index as game_index, g.board as game_board, g.turn as game_turn, g.winner as game_winner, g.state as game_state,
                g.id as game_id, g.black_player as game_black_player, g.white_player as game_white_player, g.opening_variant as game_opening_variant,
                l.*,
                user1.id as user1_id, user1.username as user1_username, user1.email as user1_email,
                user1.password as user1_password, user1.rating as user1_rating, user1.nr_games_played as user1_nr_games_played,
                user2.id as user2_id, user2.username as user2_username, user2.email as user2_email,
                user2.password as user2_password, user2.rating as user2_rating, user2.nr_games_played as user2_nr_games_played
            FROM lobbys l
            LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
            LEFT JOIN games g on l.id = g.lobby_id
            where g.id = :game_id
            """.trimIndent()
        )
            .bind("game_id", gameId)
            .mapTo<Lobby>()
            .firstOrNull()

    override fun seekLobbyID(
        otherRating: Int,
        gridSize: Int,
        winningLength: Int,
        opening: String,
        overflow: Boolean,
        minRating: Int,
        maxRating: Int
    ): Int? =
        handle.createQuery(
            """
            SELECT *
            FROM lobbys l
            LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
            LEFT JOIN games g on l.id = g.lobby_id
            where l.join_user_id is null and l.grid_size = :grid_size and l.opening = :opening and l.winning_lenght = :winning_length and l.overflow = :overflow
            AND user1.rating between :min_rating and :max_rating
            """.trimIndent()
        )
            .bind("grid_size", gridSize)
            .bind("winning_length", winningLength)
            .bind("opening", opening)
            .bind("overflow", overflow)
            .bind("min_rating", minRating)
            .bind("max_rating", maxRating)
            .mapTo<Int>()
            .firstOrNull()

    override fun getAvailableLobbies(): List<Lobby> =
        handle.createQuery(
            """
            SELECT 
                l.name as game_name, g.opening_index as game_index, g.board as game_board, g.turn as game_turn, g.winner as game_winner, g.state as game_state,
                g.id as game_id, g.black_player as game_black_player, g.white_player as game_white_player, g.opening_variant as game_opening_variant,
                l.*,
                user1.id as user1_id, user1.username as user1_username, user1.email as user1_email,
                user1.password as user1_password, user1.rating as user1_rating, user1.nr_games_played as user1_nr_games_played,
                user2.id as user2_id, user2.username as user2_username, user2.email as user2_email,
                user2.password as user2_password, user2.rating as user2_rating, user2.nr_games_played as user2_nr_games_played
            FROM lobbys l
            LEFT JOIN users as user1 ON l.creator_user_id = user1.id
            LEFT JOIN users as user2 ON l.join_user_id = user2.id
            LEFT JOIN games g on l.id = g.lobby_id
            WHERE l.join_user_id is null
            """.trimIndent()
        )
            .mapTo<Lobby>()
            .toList()
}
