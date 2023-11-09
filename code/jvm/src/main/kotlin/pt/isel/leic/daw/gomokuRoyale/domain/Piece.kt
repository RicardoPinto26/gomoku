package pt.isel.leic.daw.gomokuRoyale.domain

/**
 * Piece entity
 *
 * A piece can white be white or black
 */
enum class Piece {
    WHITE, BLACK;

    val other get() = if (this == WHITE) BLACK else WHITE

    companion object {
        fun String.toPiece() = when (this.uppercase()) {
            "WHITE" -> WHITE
            "BLACK" -> BLACK
            else -> throw Exception("Invalid piece")
        }
    }
}
