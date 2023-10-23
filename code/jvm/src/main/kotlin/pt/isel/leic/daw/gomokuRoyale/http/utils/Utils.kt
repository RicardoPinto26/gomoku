package pt.isel.leic.daw.gomokuRoyale.http.utils

import pt.isel.leic.daw.gomokuRoyale.http.media.Problem
import pt.isel.leic.daw.gomokuRoyale.services.game.GameCreationError
import pt.isel.leic.daw.gomokuRoyale.services.game.GameForfeitError
import pt.isel.leic.daw.gomokuRoyale.services.game.GameIdentificationError
import pt.isel.leic.daw.gomokuRoyale.services.game.GamePlayError
import pt.isel.leic.daw.gomokuRoyale.services.game.GameServicesError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyCreationError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyJoinError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbySeekError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyServicesError
import pt.isel.leic.daw.gomokuRoyale.services.users.GetUserStatsError
import pt.isel.leic.daw.gomokuRoyale.services.users.TokenCreationError
import pt.isel.leic.daw.gomokuRoyale.services.users.UserCreationError
import pt.isel.leic.daw.gomokuRoyale.services.users.UserServicesError
fun UserServicesError.toResponse() =
    when (this) {
        is UserCreationError -> when (this) {
            UserCreationError.InsecurePassword -> Problem.response(Problem.insecurePassword)
            UserCreationError.UserAlreadyExists -> Problem.response(Problem.userAlreadyExists)
            UserCreationError.InvalidUsername -> Problem.response(Problem.invalidUsername)
            UserCreationError.InvalidEmail -> Problem.response(Problem.invalidEmail)
        }

        is TokenCreationError -> when (this) {
            TokenCreationError.UserOrPasswordAreInvalid ->
                Problem.response(Problem.userOrPasswordIsIncorrect)
        }

        is GetUserStatsError -> when (this) {
            GetUserStatsError.NoSuchUser ->
                Problem.response(Problem.userWithUsernameNotFound)
        }
    }

fun LobbyServicesError.toResponse() = when (this) {
    is LobbyCreationError -> when (this) {
        LobbyCreationError.UserNotFound ->
            Problem.response(Problem.userWithUsernameNotFound)
    }

    is LobbyJoinError -> when (this) {
        LobbyJoinError.UserNotFound ->
            Problem.response(Problem.tokenNotFound)

        LobbyJoinError.LobbyNotFound ->
            Problem.response(Problem.lobbyNotFound)

        LobbyJoinError.LobbyFull ->
            Problem.response(Problem.lobbyFull)

        LobbyJoinError.UserAlreadyInLobby ->
            Problem.response(Problem.userAlreadyInLobby)
    }

    is LobbySeekError -> when (this) {
        LobbySeekError.UserAlreadyInALobby ->
            Problem.response(Problem.userAlreadyInALobby)
    }
}

fun GameServicesError.toResponse() = when (this) {
    is GameCreationError -> when (this) {
        GameCreationError.LobbyDoesNotExist -> Problem.response(Problem.lobbyDoesNotExist)
        GameCreationError.LobbyAlreadyHasGame -> Problem.response(Problem.lobbyAlreadyHasGame)
        GameCreationError.LobbyNotFull -> Problem.response(Problem.lobbyDoesNotExist)
        GameCreationError.UserNotInLobby -> Problem.response(Problem.userNotInLobby)
    }

    is GameForfeitError -> when (this) {
        GameForfeitError.GameAlreadyEnded -> Problem.response(Problem.gameAlreadyEnded)
        GameForfeitError.GameDoesNotExist -> Problem.response(Problem.gameDoesNotExist)
        GameForfeitError.UserNotInGame -> Problem.response(Problem.userNotInGame)
    }

    is GamePlayError -> when (this) {
        GamePlayError.GameAlreadyEnded -> Problem.response(Problem.gameAlreadyEnded)
        GamePlayError.GameDoesNotExist -> Problem.response(Problem.gameDoesNotExist)
        GamePlayError.UserNotInGame -> Problem.response(Problem.userNotInGame)
        GamePlayError.InvalidPosition -> Problem.response(Problem.invalidPosition)
        GamePlayError.PositionAlreadyPlayed -> Problem.response(Problem.occupiedPosition)
        GamePlayError.WrongTurn -> Problem.response(Problem.wrongTurn)
    }

    is GameIdentificationError -> when (this) {
        GameIdentificationError.GameDoesNotExist -> Problem.response(Problem.gameDoesNotExist)
    }
}
