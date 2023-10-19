package pt.isel.leic.daw.gomokuRoyale.http.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.model.Problem
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GameCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GameCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GameForfeitOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GamePlayInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GamePlayOutputModel
import pt.isel.leic.daw.gomokuRoyale.services.game.GameCreationError
import pt.isel.leic.daw.gomokuRoyale.services.game.GameForfeitError
import pt.isel.leic.daw.gomokuRoyale.services.game.GamePlayError
import pt.isel.leic.daw.gomokuRoyale.services.game.GameService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

@RestController
class GameController(
    val gameService: GameService
) {

    @PostMapping(Uris.Game.CREATE_GAME)
    fun createGame(
        user: AuthenticatedUser,
        @RequestBody body: GameCreateInputModel
    ): ResponseEntity<*> {
        return when (val res = gameService.createGame(body.lobbyId, user.user.id)) {
            is Success -> ResponseEntity.status(201)
                .body(GameCreateOutputModel(res.value))

            is Failure -> when (res.value) {
                GameCreationError.LobbyDoesNotExist -> Problem.response(404, Problem.lobbyDoesNotExist)
                // GameCreationError.GameWithThatNameAlreadyExists -> TODO()
                GameCreationError.LobbyAlreadyHasGame -> Problem.response(409, Problem.lobbyAlreadyHasGame)
                GameCreationError.LobbyNotFull -> Problem.response(409, Problem.lobbyDoesNotExist)
                GameCreationError.UserNotInLobby -> Problem.response(409, Problem.userNotInLobby)
                GameCreationError.UnknownError -> TODO()
            }
        }
    }

    @PostMapping(Uris.Game.FORFEIT_GAME)
    fun forfeitGame(user: AuthenticatedUser, @PathVariable gameId: Int): ResponseEntity<*> {
        return when (val res = gameService.forfeitGame(gameId, user.user.id)) {
            is Success -> ResponseEntity.status(200)
                .body(GameForfeitOutputModel(res.value))

            is Failure -> when (res.value) {
                GameForfeitError.GameAlreadyEnded -> Problem.response(409, Problem.gameAlreadyEnded)
                GameForfeitError.GameDoesNotExist -> Problem.response(404, Problem.gameDoesNotExist)
                GameForfeitError.UserNotInGame -> Problem.response(404, Problem.userNotInGame)
                GameForfeitError.UnknownError -> TODO()
            }
        }
    }

    @PostMapping(Uris.Game.PLAY_GAME)
    fun playGame(
        user: AuthenticatedUser,
        @PathVariable gameId: Int,
        @RequestBody body: GamePlayInputModel
    ): ResponseEntity<*> {
        return when (val res = gameService.playGame(gameId, user.user.id, Position(body.x, body.y))) {
            is Success -> ResponseEntity.status(200)
                .body(GamePlayOutputModel(res.value))

            is Failure -> when (res.value) {
                GamePlayError.GameAlreadyEnded -> Problem.response(409, Problem.gameAlreadyEnded)
                GamePlayError.GameDoesNotExist -> Problem.response(404, Problem.gameDoesNotExist)
                GamePlayError.UserNotInGame -> Problem.response(404, Problem.userNotInGame)
                GamePlayError.InvalidPosition -> TODO()
                GamePlayError.PositionAlreadyPlayed -> TODO()
                GamePlayError.UnknownError -> TODO()
            }
        }
    }
}
