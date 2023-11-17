package pt.isel.leic.daw.gomokuRoyale.domain

enum class Opening(val movesList: List<OpeningMove>, val variantList: List<Opening> = emptyList()) {
    FREESTYLE(emptyList(), emptyList()),
    PRO(
        listOf(
            OpeningMove.PLACE_BLACK, // center of the board
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.PLACE_WHITE, // anywhere on the board
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.PLACE_BLACK // least three intersections away from the first stone
        )
    ),
    LONG_PRO(
        listOf(
            OpeningMove.PLACE_BLACK, // center of the board
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.PLACE_WHITE, // anywhere on the board
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.PLACE_BLACK, // least four intersections away from the first stone
            OpeningMove.CHANGE_PLAYER
        )
    ),
    SWAP(
        listOf(
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_WHITE,
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.CHOOSE_COLOR
        )
    ),
    SWAP2_1(
        listOf(
            OpeningMove.PLACE_WHITE,
            OpeningMove.CHANGE_PLAYER
        )
    ),
    SWAP2_2(
        listOf(
            OpeningMove.SWAP_COLOR
        )
    ),
    SWAP2_3(
        listOf(
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_WHITE,
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.CHOOSE_COLOR
        )
    ),

    SWAP2(
        listOf(
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_WHITE,
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.CHOOSE_NEXT_MOVE
        ),
        listOf(SWAP2_1, SWAP2_2, SWAP2_3)
    );

    enum class OpeningMove {
        PLACE_WHITE, PLACE_BLACK, CHOOSE_COLOR, CHANGE_PLAYER, CHOOSE_NEXT_MOVE, SWAP_COLOR;

        companion object {
            fun placeColor(piece: Piece): OpeningMove =
                when (piece) {
                    Piece.BLACK -> PLACE_BLACK
                    Piece.WHITE -> PLACE_WHITE
                }
        }
    }

    companion object {
        fun from(opening: String): Opening? = values().find { it.name == opening }
    }
}
