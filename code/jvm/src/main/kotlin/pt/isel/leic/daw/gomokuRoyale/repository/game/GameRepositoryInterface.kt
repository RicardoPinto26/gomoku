package pt.isel.leic.daw.gomokuRoyale.repository.game

import pt.isel.leic.daw.gomokuRoyale.domain.Game

interface GameRepositoryInterface {

    fun createGame(name: String, lobbyId: Int, playerBlack: Int, playerWhite: Int): Int

    fun getGameById(gameId: Int): Game?

    fun getGameByLobbyId(lobbyId: Int): Game?

    fun getGameByPlayerIdAndLobbyId(playerId: Int, lobbyId: Int): Game?
}