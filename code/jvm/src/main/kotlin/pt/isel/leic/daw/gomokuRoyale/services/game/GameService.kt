package pt.isel.leic.daw.gomokuRoyale.services.game

interface GameService {
    fun createGame(name: String, user1: Int, user2: Int, lobbyId: Int): GameCreationResult

    fun forfeitGame(gameId: Int, user: Int): GameForfeitResult

    //fun playGame(gameId: Int, user: Int, position: Position): GamePlayResult

    fun getGameByLobbyId(lobbyId: Int): Int?
}