package pt.isel.leic.daw.gomokuRoyale.domain

/**
 * Piece entity
 *
 * A piece can white be white or black
 */
enum class Piece {
    WHITE, BLACK;

    val other get() = if (this == WHITE) BLACK else WHITE
}
