package pt.isel.leic.daw.gomokuRoyale.controllers.model

import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.UserStats

data class UserGetStatisticsOutputModel(val gamesPlayed: Int, val rating: Int) {
    constructor(userStats: UserStats) : this(userStats.gamesPlayed, userStats.rating)
}