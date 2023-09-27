package pt.isel.leic.daw.gomokuRoyale.domain

data class GameSettings(
        val boardSize: Int,
        val winningLength: Int,
        private val opening: Opening,
        val overflowAllowed: Boolean
) {
    init {
        require(winningLength <= boardSize)
    }
}