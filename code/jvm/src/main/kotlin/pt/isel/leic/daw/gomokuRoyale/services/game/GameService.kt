package pt.isel.leic.daw.gomokuRoyale.services.game

import pt.isel.leic.daw.gomokuRoyale.domain.Game

interface GameService {
    fun createGame(name: String, tokenUser1: String, tokenUser2: String, lobbyId: Int): GameCreationResult

    fun forfeitGame(gameId: Int, token: String): GameForfeitResult

    // fun playGame(gameId: Int, user: Int, position: Position): GamePlayResult

    fun getGameByLobbyId(lobbyId: Int): Game?
}
