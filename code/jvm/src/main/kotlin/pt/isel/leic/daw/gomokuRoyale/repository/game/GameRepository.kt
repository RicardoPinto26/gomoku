package pt.isel.leic.daw.gomokuRoyale.repository.game

import pt.isel.leic.daw.gomokuRoyale.domain.Game

interface GameRepository {

    fun createGame(lobbyId: Int, turn: Int, board: String): Int

    fun getGameById(gameId: Int): Game?

    fun getGameByLobbyId(lobbyId: Int): Game?

    fun updateGameDraw(gameId: Int, board: String): Int

    fun updateGameWinner(gameId: Int, winner: Int, board: String): Int

    fun updateGameBoard(gameId: Int, turn: Int, board: String): Int
}
