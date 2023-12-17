package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models.LobbyCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models.LobbyCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models.LobbyDetailsOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models.LobbyJoinOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models.LobbySeekInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models.LobbySeekOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.UserController
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.SirenEntity
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.SubEntity
import pt.isel.leic.daw.gomokuRoyale.http.utils.Actions
import pt.isel.leic.daw.gomokuRoyale.http.utils.Links
import pt.isel.leic.daw.gomokuRoyale.http.utils.Rels
import pt.isel.leic.daw.gomokuRoyale.http.utils.Uris
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
    // TODO: SIREN
    // TODO: DO WE EVEN NEED TO ALLOW JOIN/CREATE LOBBY? ISN'T MATCHMAKING ENOUGH?
    @PostMapping(Uris.Lobby.CREATE_LOBBY)
    fun createLobby(
        user: AuthenticatedUser,
        @RequestBody body: LobbyCreateInputModel
    ): ResponseEntity<*> {
        logger.info("Request from user ${user.user.username} to create lobby received")
        logger.info("${body.gridSize}")
        logger.info(body.opening)
        logger.info(body.winningLength.toString())
        logger.info("${body.pointsMargin}")
        return when (
            val res = lobbyService.createLobby(
                user.user,
                body.name,
                body.gridSize,
                body.opening,
                body.winningLength,
                body.pointsMargin,
                body.overflow
            )
        ) {
            is Success -> {
                logger.info("Successful Request")
                ResponseEntity.status(201)
                    .body(
                        SirenEntity(
                            `class` = listOf(Rels.CREATE_LOBBY),
                            properties = LobbyCreateOutputModel(res.value)
                        )
                    )
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
                    .body(
                        SirenEntity(
                            `class` = listOf(Rels.JOIN_LOBBY),
                            properties = LobbyJoinOutputModel(res.value),
                            entities = listOf(
                                SubEntity.EmbeddedLink(
                                    `class` = listOf(Rels.GAME),
                                    rel = listOf(Rels.ITEM, Rels.GAME),
                                    href = Uris.Game.byId(res.value.lobbyId, res.value.gameId)
                                )
                            )
                        )
                    )
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
                body.overflow,
                body.pointsMargin
            )
        ) {
            is Success -> {
                logger.info("Successful Request")
                val lobbySEI = res.value
                ResponseEntity.status(if (lobbySEI.user2 == null) 201 else 200)
                    .body(
                        if (lobbySEI.user2 == null) {
                            SirenEntity(
                                `class` = listOf(Rels.SEEK_LOBBY),
                                properties = LobbySeekOutputModel(lobbySEI)
                            )
                        } else {
                            SirenEntity(
                                `class` = listOf(Rels.SEEK_LOBBY),
                                properties = LobbySeekOutputModel(lobbySEI),
                                entities = if (res.value.game == null) {
                                    null
                                } else {
                                    listOf(
                                        SubEntity.EmbeddedSubEntity(
                                            `class` = listOf(Rels.GAME),
                                            rel = listOf(Rels.ITEM, Rels.GAME),
                                            properties = res.value.game,
                                            /*actions = listOf(
                                                Actions.play,
                                                Actions.forfeitGame
                                            )*/
                                            links = listOf(
                                                Links.self(Uris.Game.byId(res.value.id, res.value.game.id))
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    )
            }

            is Failure -> {
                logger.info("Failed Request")
                res.value.toResponse()
            }
        }
    }

    // TODO: IF WE DON'T ALLOW CREATE/JOIN LOBBY, THIS IS USELESS
    @GetMapping(Uris.Lobby.GET_AVAILABLE_LOBBIES)
    fun getLobbies(user: AuthenticatedUser): ResponseEntity<*> {
        return when (val res = lobbyService.getAvailableLobbies(user.user)) {
            is Success -> {
                logger.info("Successful Request")
                ResponseEntity.status(200)
                    .body(
                        SirenEntity<Unit>(
                            `class` = listOf(Rels.LIST_LOBBIES),
                            entities = res.value.lobbies.map {
                                SubEntity.EmbeddedSubEntity(
                                    `class` = listOf(Rels.LOBBY),
                                    rel = listOf(Rels.ITEM, Rels.LOBBY),
                                    properties = LobbyDetailsOutputModel(it),
                                    links = listOf(
                                        Links.self(Uris.Lobby.byId(it.id))
                                    ),
                                    entities = if (it.game == null) {
                                        null
                                    } else {
                                        listOf(
                                            SubEntity.EmbeddedSubEntity(
                                                `class` = listOf(Rels.GAME),
                                                rel = listOf(Rels.ITEM, Rels.GAME),
                                                properties = it.game,
                                                /*actions = listOf(
                                                    Actions.play,
                                                    Actions.forfeitGame
                                                )*/
                                            )
                                        )
                                    }
                                )
                            }
                        )
                    )
            }

            is Failure -> {
                logger.info("Failed Request")
                res.value.toResponse()
            }
        }
    }

    @GetMapping(Uris.Lobby.GET_LOBBY_DETAILS)
    fun getLobbyDetails(
        user: AuthenticatedUser,
        @PathVariable lobbyId: Int
    ): ResponseEntity<*> {
        return when (val res = lobbyService.getLobbyDetails(user.user, lobbyId)) {
            is Success -> {
                logger.info("Successful Request")
                ResponseEntity.status(200)
                    .body(
                        SirenEntity(
                            `class` = listOf(Rels.LOBBY),
                            properties = LobbyDetailsOutputModel(res.value),
                            links = listOf(
                                Links.self(Uris.Lobby.byId(res.value.id))
                            ),
                            entities = if (res.value.game == null) {
                                logger.info("game == null in getLobbyDetails")
                                null
                            } else {
                                logger.info("game not null   in getLobbyDetails")
                                try {
                                    listOf(
                                        SubEntity.EmbeddedSubEntity(
                                            `class` = listOf(Rels.GAME),
                                            rel = listOf(Rels.ITEM, Rels.GAME),
                                            properties = res.value.game,
                                            /*actions = listOf(
                                                Actions.play,
                                                Actions.forfeitGame
                                            )*/ // TAVA  A DAR EXCEPTION SEM MOTIVO
                                            links = listOf(
                                                Links.self(Uris.Game.byId(res.value.id, res.value.game.id))
                                            )
                                        )
                                    )
                                } catch (e: Exception) {
                                    logger.info("Exception in getLobbyDetails $e")
                                    null
                                }
                            }
                        )
                    )
            }

            is Failure -> {
                logger.info("Failed Request")
                res.value.toResponse()
            }
        }
    }

    /**
     * if (lobbySEI.usernameJoin == null) {
     *                             SirenEntity(
     *                                 `class` = listOf(Rels.SEEK_LOBBY),
     *                                 properties = LobbySeekOutputModel(lobbySEI)
     *                             )
     *                         } else {
     *                             SirenEntity(
     *                                 `class` = listOf(Rels.SEEK_LOBBY),
     *                                 properties = LobbySeekOutputModel(lobbySEI),
     *                                 entities = listOf(
     *                                     SubEntity.EmbeddedLink(
     *                                         `class` = listOf(Rels.GAME),
     *                                         rel = listOf(Rels.ITEM, Rels.GAME),
     *                                         href = Uris.Game.byId(lobbySEI.lobbyId,lobbySEI.gameId!!)
     *                                     )
     *                                 )
     *                             )
     *                         }
     */

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
