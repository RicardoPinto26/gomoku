package pt.isel.leic.daw.gomokuRoyale.http.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.model.Problem
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyJoinOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbySeekInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbySeekOutputModel
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyCreationError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyJoinError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbySeekError
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

@RestController
class LobbyController(
    private val lobbyService: LobbyService
) {
    @PostMapping(Uris.Lobby.CREATE_LOBBY)
    fun createLobby(
        user: AuthenticatedUser,
        @RequestBody body: LobbyCreateInputModel
    ): ResponseEntity<*> {
        logger.info("Request from user ${user.user.username} to create lobby received")
        logger.info("${body.gridSize}")
        logger.info(body.opening)
        logger.info(body.variant)
        logger.info("${body.pointsMargin}")
        return when (
            val res = lobbyService.createLobby(
                body.name,
                user.token,
                body.gridSize,
                body.opening,
                body.variant,
                body.pointsMargin
            )
        ) {
            is Success -> {
                logger.info("Successful Request")
                ResponseEntity.status(201)
                    .body(LobbyCreateOutputModel(res.value))
            }

            is Failure -> {
                logger.info("Failed Request")
                when (res.value) {
                    LobbyCreationError.UserNotFound ->
                        Problem.response(404, Problem.userWithUsernameNotFound)
                }
            }
        }
    }

    @PostMapping(Uris.Lobby.JOIN_LOBBY)
    fun joinLobby(
        user: AuthenticatedUser,
        @PathVariable lobbyId: String
    ): ResponseEntity<*> {
        logger.info("Request to join lobby with id $lobbyId")
        return when (val res = lobbyService.joinLobby(user.token, lobbyId.toInt())) {
            is Success -> {
                logger.info("Successful Request")
                ResponseEntity.status(200)
                    .body(LobbyJoinOutputModel(res.value))
            }

            is Failure -> {
                logger.info("Failed Request")
                when (res.value) {
                    LobbyJoinError.UserNotFound ->
                        Problem.response(404, Problem.userWithUsernameNotFound)

                    LobbyJoinError.LobbyNotFound ->
                        Problem.response(404, Problem.lobbyNotFound)

                    LobbyJoinError.LobbyFull ->
                        Problem.response(409, Problem.lobbyFull)

                    LobbyJoinError.UserAlreadyInLobby ->
                        Problem.response(409, Problem.userAlreadyInLobby)
                }
            }
        }
    }

    @PostMapping(Uris.Lobby.SEEK_LOBBY)
    fun seekLobby(
        user: AuthenticatedUser,
        @RequestBody body: LobbySeekInputModel
    ): ResponseEntity<*> {
        return when (
            val res = lobbyService.seekLobby(
                user.user,
                body.gridSize,
                body.winningLength,
                body.opening,
                body.pointsMargin
            )
        ) {
            is Success -> {
                logger.info("Successful Request")
                val lobbyEI = res.value
                ResponseEntity.status(if (lobbyEI.user2 == null) 201 else 200)
                    .body(LobbySeekOutputModel(lobbyEI))
            }

            is Failure -> {
                logger.info("Failed Request")
                when (res.value) {
                    LobbySeekError.UserAlreadyInALobby ->
                        Problem.response(409, Problem.userAlreadyInALobby)
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
