package pt.isel.leic.daw.gomokuRoyale.http

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.http.model.Problem
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyCreationError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyJoinError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyJoinExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyServiceImpl
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

@RestController
class LobbyController(
    private val lobbyService: LobbyServiceImpl
){
    @PostMapping(Uris.Lobby.CREATE_LOBBY)
    fun createLobby(
        token: String, gridSize: Int, opening: String, variant: String, pointsMargin: Int
    ): ResponseEntity<*> {
        return when (val res = lobbyService.createLobby(token, gridSize, opening, variant, pointsMargin)) {
            is Success ->
                ResponseEntity.status(201)
                    .body(LobbyCreateOutputModel(res.value))

            is Failure -> when (res.value) {
                LobbyCreationError.UserNotFound ->
                    Problem.response(404, Problem.userWithUsernameNotFound)
                LobbyCreationError.UnknownError ->
                    Problem.response(500, Problem.unknowError)
            }
        }
    }

    @PostMapping(Uris.Lobby.JOIN_LOBBY)
    fun joinLobby(
        token: String, lobbyId: Int
    ): ResponseEntity<*>{
        return when( val res = lobbyService.joinLobby(token, lobbyId)){
            is Success ->
                ResponseEntity.status(200)
                    .body(LobbyJoinOutputModel(res.value))

            is Failure -> when (res.value) {
                LobbyJoinError.UserNotFound ->
                    Problem.response(404, Problem.userWithUsernameNotFound)
                LobbyJoinError.LobbyNotFound ->
                    Problem.response( 404,Problem.lobbyNotFound)
                LobbyJoinError.LobbyFull ->
                    Problem.response(409, Problem.lobbyFull)
                LobbyJoinError.UserAlreadyInLobby ->
                    Problem.response(409, Problem.userAlreadyInLobby)
                LobbyJoinError.UnknownError ->
                    Problem.response(500, Problem.unknowError)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UsersController::class.java)
    }
}

data class LobbyJoinOutputModel(
    val usernameCreator: String,
    val usernameJoin: String,
    val lobbyId: Int
){
    constructor(uei: LobbyJoinExternalInfo) : this(uei.usernameCreator, uei.usernameJoin, uei.lobbyId)
}

