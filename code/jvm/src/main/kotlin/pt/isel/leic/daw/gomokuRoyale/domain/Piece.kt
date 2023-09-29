package pt.isel.leic.daw.gomokuRoyale.domain

enum class Piece {
    WHITE, BLACK;

    val other get() = if(this == WHITE) BLACK else WHITE
}
