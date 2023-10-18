package pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers

import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import java.sql.ResultSet

class UserMapper {
    fun map(rs: ResultSet, prefix: String): User {
        val id = rs.getInt(prefix + "id")
        val username = rs.getString(prefix + "username")
        val email = rs.getString(prefix + "email")
        val password = rs.getString(prefix + "password")
        val rating = rs.getDouble(prefix + "rating")
        val nrGamesPlayed = rs.getInt(prefix + "nr_games_played")

        return User(id, username, email, password, rating, nrGamesPlayed)
    }
}