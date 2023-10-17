package pt.isel.leic.daw.gomokuRoyale.http.model.user

import pt.isel.leic.daw.gomokuRoyale.services.users.PublicUserExternalInfo

data class UserGetStatisticsOutputModel(val username: String, val gamesPlayed: Int, val rating: Int) {
    constructor(uei: PublicUserExternalInfo) : this(uei.username, uei.gamesPlayed, uei.rating)
}
