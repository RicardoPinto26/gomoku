package pt.isel.leic.daw.gomokuRoyale.repository.game

import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.user.GameDTO

interface GameRepository {

    fun createGame(lobbyId: Int, turn: Int, blackPlayer: Int, whitePlayer: Int, openingIndex: Int, board: String): Int

    fun getGameById(gameId: Int): Game?

    fun getGameByLobbyId(lobbyId: Int): GameDTO?

    fun updateGameDraw(gameId: Int, board: String): Int

    fun updateGameWinner(gameId: Int, winner: Int, board: String): Int

    fun updateGamePlayer(gameId: Int, newPlayer1: Int, newPlayer2: Int, turn: Int, newOpeningIndex: Int): Int

    fun updateGameBoard(gameId: Int, turn: Int, board: String, openingIndex: Int): Int

    fun updateOpeningIndex(gameId: Int, openingIndex: Int): Int

    fun updateOpeningVariant(gameId: Int, openingVariant: String, openingIndex: Int): Int
}
