package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.Piece.Companion.toPiece
import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.services.game.GameAction

/**
 * Game move input information
 *
 * @property actionType the type of action being performed
 * @property positionX the x coordinate of the position being played
 * @property positionY the y coordinate of the position being played
 * @property moveChoice the move being chosen
 * @property color the color being chosen
 */

data class GamePlayInputModel(
    val actionType: String, // "PlacePiece", "ChooseMove", or "ChooseColor"
    val positionX: Int?, // used for PlacePiece_x
    val positionY: Int?, // used for PlacePiece_y
    val moveChoice: String?, // used for ChooseMove
    val color: String? // used for ChooseColor
)

fun convertInputModelToGameAction(input: GamePlayInputModel): GameAction {
    return when (input.actionType) {
        "PlacePiece" -> {
            val position = Position(
                input.positionX ?: throw Exception("Invalid position"),
                input.positionY ?: throw Exception("Invalid position")
            )
            GameAction.PlacePiece(position)
        }

        "ChooseMove" -> {
            val move = input.moveChoice ?: throw Exception("Invalid move choice")
            GameAction.ChooseMove(Opening.valueOf(move))
        }

        "ChooseColor" -> {
            input.color?.let { GameAction.ChooseColor(it.toPiece()) } ?: throw Exception("Invalid color")
        }

        else ->
            throw IllegalStateException("Invalid action type")
    }
}
