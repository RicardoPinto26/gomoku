package pt.isel.leic.daw.gomokuRoyale.repository.game

import pt.isel.leic.daw.gomokuRoyale.domain.Board
import pt.isel.leic.daw.gomokuRoyale.domain.Game


interface GameStateRepositoryInterface {
    fun createGameState(gameId: Int, turn: Int): Int

    fun getGameStateByGameId(gameId: Int): Game?

    fun updateGameState(gameId: Int, board: Board): Int

    fun updateGameStateWinner(gameId: Int, winner: Int): Int

    fun forfeitGame(gameId: Int, user: Int): Int

    fun deleteGameState(gameId: Int): Int

}