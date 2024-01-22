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
        method = Method.DELETE,
        href = Uris.logoutUser()
    )

    val seekLobby = Action(
        name = Rels.SEEK_LOBBY,
        title = "Seek Lobby",
        method = Method.POST,
        href = Uris.seekLobby()
    )

    val createLobby = Action(
        name = Rels.CREATE_LOBBY,
        title = "Create Lobby",
        method = Method.POST,
        href = Uris.createLobby()
    )

    val listLobbies = Action(
        name = Rels.LIST_LOBBIES,
        title = "List Lobbies",
        method = Method.GET,
        href = Uris.listLobbies()
    )

    fun joinLobby(lobbyID: Int) = Action(
        name = Rels.JOIN_LOBBY,
        title = "Join Lobby",
        method = Method.POST,
        href = Uris.joinLobby(lobbyID)
    )

    fun play(lobbyID: Int, gameID: Int) = Action(
        name = Rels.PLAY,
        title = "Play",
        method = Method.POST,
        href = Uris.play(lobbyID, gameID)
    )

    fun forfeitGame(lobbyID: Int, gameID: Int) = Action(
        name = Rels.FORFEIT_GAME,
        title = "Forfeit Game",
        method = Method.POST,
        href = Uris.forfeitGame(lobbyID, gameID)
    )
}
