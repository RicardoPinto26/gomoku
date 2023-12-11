package pt.isel.leic.daw.gomokuRoyale.http.utils

import pt.isel.leic.daw.gomokuRoyale.http.media.siren.Action
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.Method

object Actions {

    val listUsers = Action(
        name = Rels.LIST_USERS,
        title = "List Users",
        method = Method.GET,
        href = Uris.users()
    )

    val register = Action(
        name = Rels.REGISTER,
        title = "Register",
        method = Method.POST,
        href = Uris.users()
    )

    val login = Action(
        name = Rels.LOGIN,
        title = "Login",
        method = Method.POST,
        href = Uris.loginUser()
    )

    val logout = Action(
        name = Rels.LOGOUT,
        title = "Logout",
        method = Method.POST,
        href = Uris.logoutUser()
    )

    val seekLobby = Action(
        name = Rels.SEEK_LOBBY,
        title = "Seek Lobby",
        method = Method.POST,
        href = Uris.seekLobby()
    )

    val listLobbies = Action(
        name = Rels.LIST_LOBBIES,
        title = "List Lobbies",
        method = Method.GET,
        href = Uris.listLobbies()
    )

    val play = Action(
        name = Rels.PLAY,
        title = "Play",
        method = Method.POST,
        href = Uris.play()
    )

    val forfeitGame = Action(
        name = Rels.FORFEIT_GAME,
        title = "Forfeit Game",
        method = Method.POST,
        href = Uris.forfeitGame()
    )
}
