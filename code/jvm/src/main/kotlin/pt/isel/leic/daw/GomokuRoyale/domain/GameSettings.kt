package pt.isel.leic.daw.GomokuRoyale.domain

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