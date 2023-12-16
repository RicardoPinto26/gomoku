package pt.isel.leic.daw.gomokuRoyale.http.controllers.games

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models.GameCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models.GameDetailsOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models.GameForfeitOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models.GamePlayInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models.GamePlayOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models.convertInputModelToGameAction
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.SirenEntity
import pt.isel.leic.daw.gomokuRoyale.http.utils.Rels
import pt.isel.leic.daw.gomokuRoyale.http.utils.Uris
import pt.isel.leic.daw.gomokuRoyale.http.utils.toResponse
import pt.isel.leic.daw.gomokuRoyale.services.game.GameService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

/**
 * Controller that handles the requests related to games
 *
 * @property gameService services that deals with the game business logic
 */
@RestController
class GameController(
    val gameService: GameService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(GameController::class.java)
    }

    /**
     * Handles the request to create a game.
     *
     * @param user the [AuthenticatedUser] creating the game
     * @param lobbyId the unique id of the lobby hosting the game
     *
     * @return the response to the request with the [GameCreateOutputModel] in the body or an error value
     */
    // TODO: SIREN
    // TODO: IF WE DON'T ALLOW CREATE/JOIN LOBBY, THIS IS USELESS
    @PostMapping(Uris.Game.CREATE_GAME)
    fun createGame(
        user: AuthenticatedUser,
        @PathVariable lobbyId: Int
    ): ResponseEntity<*> {
        return when (val res = gameService.createGame(lobbyId, user.user.id)) {
            is Success -> ResponseEntity.status(201)
                .body(GameCreateOutputModel(res.value))

            is Failure -> res.value.toResponse()
        }
    }

    /**
     * Handles the request to forfeit a game.
     *
     * @param user the [AuthenticatedUser] creating the game
     * @param lobbyId the unique id of the lobby hosting the game
     * @param gameId the unique id of the game being forfeited
     *
     * @return the response to the request with the [GameForfeitOutputModel] in the body or an error value
     */
    @PostMapping(Uris.Game.FORFEIT_GAME)
    fun forfeitGame(
        user: AuthenticatedUser,
        @PathVariable lobbyId: Int,
        @PathVariable gameId: Int
    ): ResponseEntity<*> {
        return when (val res = gameService.forfeitGame(gameId, user.user.id)) {
            is Success -> ResponseEntity.status(200)
                .body(
                    SirenEntity(
                        `class` = listOf(Rels.FORFEIT_GAME),
                        properties = GameForfeitOutputModel(res.value)
                    )
                )

            is Failure -> res.value.toResponse()
        }
    }

    /**
     * Handles the request to forfeit a game.
     *
     * @param user the [AuthenticatedUser] creating the game
     * @param lobbyId the unique id of the lobby hosting the game
     * @param gameId the unique id of the game being played
     * @param body the [GamePlayInputModel] with the move being made
     *
     * @return the response to the request with the [GamePlayOutputModel] in the body or an error value
     */
    @PostMapping(Uris.Game.PLAY_GAME)
    fun playGame(
        user: AuthenticatedUser,
        @PathVariable gameId: Int,
        @PathVariable lobbyId: String,
        @RequestBody body: GamePlayInputModel
    ): ResponseEntity<*> {
        val action = convertInputModelToGameAction(body)
        return when (val res = gameService.playGame(gameId, user.user.id, action)) {
            is Success -> ResponseEntity.status(200)
                .body(
                    SirenEntity(
                        `class` = listOf(Rels.PLAY),
                        properties = GamePlayOutputModel(res.value)
                    )
                )

            is Failure -> res.value.toResponse()
        }
    }

    /**
     * Handles the request to get a game.
     *
     * @param gameId the unique id of the game being forfeited
     * @param lobbyId the unique id of the lobby hosting the game
     *
     * @return the response to the request with the [GameDetailsOutputModel] in the body or an error value
     */
    @GetMapping(Uris.Game.GET_GAME)
    fun getGameById(@PathVariable gameId: Int, @PathVariable lobbyId: Int): ResponseEntity<*> {
        try {
            return when (val res = gameService.getGameById(gameId, lobbyId)) {
                is Success -> ResponseEntity.status(200)
                    .body(
                        SirenEntity(
                            `class` = listOf(Rels.GAME),
                            properties = GameDetailsOutputModel(res.value)
                            /*actions = listOf(
                                Actions.play,
                                Actions.forfeitGame
                            )*/
                        )
                    )

                is Failure -> res.value.toResponse()
            }
        } catch (e: Exception) {
            logger.info("Error getting game by id", e)
            return ResponseEntity.status(500).body(e.message)
        }
    }
}
