package pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models

import pt.isel.leic.daw.gomokuRoyale.services.users.PublicUserExternalInfo

/**
 * User statistics output information
 *
 * @property username the username of the user
 * @property gamesPlayed number of games played by the user
 * @property rating the user's rating
 *
 */
data class UserGetStatisticsOutputModel(val username: String, val gamesPlayed: Int, val rating: Int) {
    constructor(uei: PublicUserExternalInfo) : this(uei.username, uei.gamesPlayed, uei.rating)
}
