package pt.isel.leic.daw.gomokuRoyale.domain

/**
 * Game settings
 *
 * @property boardSize the size of the board for the game
 * @property winningLength amount of pieces in a line needed to win a game
 * @property opening the type of [Opening]
 * @property overflowAllowed whether amount of pieces to win must be (exactly)/(at least) winningLength
 */
data class GameSettings(
    val boardSize: Int,
    val winningLength: Int,
    val opening: Opening,
    val overflowAllowed: Boolean = true
) {
    init {
        // require(winningLength <= boardSize)
    }
}
