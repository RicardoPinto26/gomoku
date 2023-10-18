package pt.isel.leic.daw.gomokuRoyale.repository.game

import pt.isel.leic.daw.gomokuRoyale.domain.Game

interface GameRepositoryInterface {

    fun createGame(lobbyId: Int, turn: Int, board: String): Int

    fun getGameById(gameId: Int): Game?

    fun getGameByLobbyId(lobbyId: Int): Game?

    fun updateGameWinner(gameId: Int, winner: Int): Int

    fun updateGameBoard(gameId: Int, turn: Int, board: String): Int

}
