package pt.isel.leic.daw.gomokuRoyale.services.game

interface GameService {
    fun createGame(lobbyId: Int, userId: Int): GameCreationResult

    fun forfeitGame(gameId: Int, userId: Int): GameForfeitResult

    fun playGame(gameId: Int, userId: Int, action: GameAction): GamePlayResult

    fun getGameById(gameId: Int, lobbyId: Int): GameIdentificationResult

    // fun getGameByLobbyId(lobbyId: Int): Game?
}
