package pt.isel.leic.daw.gomokuRoyale.http

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uris {

    const val PREFIX = "/api"
    const val HOME = PREFIX

    fun home(): URI = URI(HOME)

    object Users {
        const val CREATE = "$PREFIX/users"
        const val DETAILS = "$PREFIX/users/{username}"
        const val TOKEN = "$PREFIX/users/token"
        const val LOGOUT = "$PREFIX/logout"
        const val STATS = "$PREFIX/users/stats"
        // const val HOME = "$PREFIX/me"

        fun byUsername(username: String) = UriTemplate(DETAILS).expand(username)

        // fun home(): URI = URI(HOME)
        fun login(): URI = URI(TOKEN)
        fun register(): URI = URI(CREATE)
    }

    object Lobby {
        const val CREATE_LOBBY = "$PREFIX/lobby"
        const val JOIN_LOBBY = "$PREFIX/lobby/{lobbyId}"
        //const val GET_LOBBY_DETAILS = "$PREFIX/lobby/{lobbyId}"
    }
}
