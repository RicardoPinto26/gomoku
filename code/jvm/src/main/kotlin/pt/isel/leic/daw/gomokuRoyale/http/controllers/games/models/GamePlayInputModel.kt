package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

/**
 * Game move input information
 *
 * @property x column
 * @property y row
 */
data class GamePlayInputModel(
    val x: Int,
    val y: Int
)
