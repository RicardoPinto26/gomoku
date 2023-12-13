package pt.isel.leic.daw.gomokuRoyale.http.utils

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uris {

    const val PREFIX = "/api"
    const val HOME = PREFIX

    fun home(): URI = URI(HOME)
    fun users(): URI = URI(Users.PREFIX)
    fun userHome(): URI = URI(Users.HOME)
    fun loginUser(): URI = URI(Users.TOKEN)
    fun logoutUser(): URI = URI(Users.LOGOUT)

    fun seekLobby(): URI = URI(Lobby.SEEK_LOBBY)
    fun listLobbies(): URI = URI(Lobby.GET_AVAILABLE_LOBBIES)

    fun play(): URI = URI(Game.PLAY_GAME)
    fun forfeitGame(): URI = URI(Game.FORFEIT_GAME)

    object Users {
        const val PREFIX = "${Uris.PREFIX}/users"
        const val HOME = "$PREFIX/home"
        const val DETAILS = "$PREFIX/{username}"
        const val TOKEN = "$PREFIX/token"
        const val LOGOUT = "$PREFIX/logout"

        fun byUsername(username: String) = UriTemplate(DETAILS).expand(username)
    }

    object Lobby {
        const val CREATE_LOBBY = "$PREFIX/lobby"
        const val JOIN_LOBBY = "$PREFIX/lobby/{lobbyId}/join"

        const val GET_LOBBY_DETAILS = "$PREFIX/lobby/{lobbyId}"
        const val SEEK_LOBBY = "$PREFIX/lobby/seek"
        const val GET_AVAILABLE_LOBBIES = "$PREFIX/lobby/available"
        const val GET_LOBBY_STATE = "$PREFIX/lobby/{lobbyId}/state"

        fun byId(id: Int) = UriTemplate(GET_LOBBY_DETAILS).expand(id)
    }

    object Game {
        const val CREATE_GAME = "$PREFIX/lobby/{lobbyId}/game/create"
        const val FORFEIT_GAME = "$PREFIX/lobby/{lobbyId}/game/{gameId}/forfeit"
        const val PLAY_GAME = "$PREFIX/lobby/{lobbyId}/game/{gameId}/play"
        const val GET_GAME = "$PREFIX/lobby/{lobbyId}/game/{gameId}"

        fun byId(lobbyId: Int, gameId: Int) = UriTemplate(GET_GAME).expand(lobbyId, gameId)
    }
}