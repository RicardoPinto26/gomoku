package pt.isel.leic.daw.gomokuRoyale.http.model

import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

data class UserGetStatisticsOutputModel(val username: String, val gamesPlayed: Int, val rating: Int) {
    constructor(uei: UserExternalInfo) : this(uei.username, uei.gamesPlayed, uei.rating)
}