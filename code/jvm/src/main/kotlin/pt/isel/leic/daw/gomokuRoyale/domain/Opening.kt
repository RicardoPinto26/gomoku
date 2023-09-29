package pt.isel.leic.daw.gomokuRoyale.domain

/**
 * ONLY AN IDEA ON HOW THE OPENING SYSTEM COULD WORK
 */
enum class Opening(val movesList: List<OpeningMove>) {

    FREESTYLE(emptyList()),
    SWAP(
        listOf(
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_BLACK,
            OpeningMove.PLACE_WHITE,
            OpeningMove.CHANGE_PLAYER,
            OpeningMove.CHOOSE_COLOR
        )
    );

    enum class OpeningMove {
        PLACE_WHITE, PLACE_BLACK, CHOOSE_COLOR, CHANGE_PLAYER
    }
}
