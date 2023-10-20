package pt.isel.leic.daw.gomokuRoyale.http.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GameCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GameDetailsOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GameForfeitOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GamePlayInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.game.GamePlayOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.utils.toResponse
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
        @PathVariable lobbyID: Int
    ): ResponseEntity<*> {
        return when (val res = gameService.createGame(lobbyID, user.user.id)) {
            is Success -> ResponseEntity.status(201)
                .body(GameCreateOutputModel(res.value))

            is Failure -> res.value.toResponse()
        }
    }

    @PostMapping(Uris.Game.FORFEIT_GAME)
    fun forfeitGame(
        user: AuthenticatedUser,
        @PathVariable lobbyID: Int,
        @PathVariable gameId: Int
    ): ResponseEntity<*> {
        return when (val res = gameService.forfeitGame(gameId, user.user.id)) {
            is Success -> ResponseEntity.status(200)
                .body(GameForfeitOutputModel(res.value))

            is Failure -> res.value.toResponse()
        }
    }

    @PostMapping(Uris.Game.PLAY_GAME)
    fun playGame(
        user: AuthenticatedUser,
        @PathVariable gameId: Int,
        @PathVariable lobbyID: String,
        @RequestBody body: GamePlayInputModel
    ): ResponseEntity<*> {
        return when (val res = gameService.playGame(gameId, user.user.id, Position(body.x, body.y))) {
            is Success -> ResponseEntity.status(200)
                .body(GamePlayOutputModel(res.value))

            is Failure -> res.value.toResponse()
        }
    }

    @GetMapping(Uris.Game.GET_GAME)
    fun getGameById(@PathVariable gameId: Int, @PathVariable lobbyID: Int): ResponseEntity<*> {
        return when (val res = gameService.getGameById(gameId, lobbyID)) {
            is Success -> ResponseEntity.status(200)
                .body(GameDetailsOutputModel(res.value))

            is Failure -> res.value.toResponse()
        }
    }
}
