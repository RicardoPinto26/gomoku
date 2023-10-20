package pt.isel.leic.daw.gomokuRoyale.http.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbyJoinOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbySeekInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.lobby.LobbySeekOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.utils.toResponse
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

/**
 * Controller that handles the requests to the /lobby endpoint
 *
 * @property lobbyService services that deals with the lobby business logic
 */
@RestController
class LobbyController(
    private val lobbyService: LobbyService
) {

    /**
     * Handles the request to create a lobby.
     *
     * @param user the [AuthenticatedUser] creating the game
     * @param body the [LobbyCreateInputModel] with the lobby information
     *
     * @return the response to the request with the [LobbyCreateOutputModel] in the body or an error value
     */
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
                user.user,
                body.name,
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
                res.value.toResponse()
            }
        }
    }

    /**
     * Handles the request to join a lobby.
     *
     * @param user the [AuthenticatedUser] creating the game
     * @param lobbyId the unique id of the lobby being joined
     *
     * @return the response to the request with the [LobbyJoinOutputModel] in the body or an error value
     */
    @PostMapping(Uris.Lobby.JOIN_LOBBY)
    fun joinLobby(
        user: AuthenticatedUser,
        @PathVariable lobbyId: String
    ): ResponseEntity<*> {
        logger.info("Request to join lobby with id $lobbyId")
        return when (val res = lobbyService.joinLobby(user.user, lobbyId.toInt())) {
            is Success -> {
                logger.info("Successful Request")
                ResponseEntity.status(200)
                    .body(LobbyJoinOutputModel(res.value))
            }

            is Failure -> {
                logger.info("Failed Request")
                res.value.toResponse()
            }
        }
    }

    /**
     * Handles the request to seek a lobby.
     *
     * @param user the [AuthenticatedUser] creating the game
     * @param body the [LobbySeekInputModel] with the lobby information
     *
     * @return the response to the request with the [LobbySeekOutputModel] in the body or an error value
     */
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
                res.value.toResponse()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
