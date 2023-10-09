package pt.isel.leic.daw.gomokuRoyale.controllers

import org.springframework.web.util.UriTemplate
import java.net.URI

// TODO: MUDA AI O NOME DO FICHEIRO PARA Uris.kt
// TODO: MUDA AI A PASTA DE controllers PARA http

object Uris {

    const val PREFIX = "/api"
    const val HOME = PREFIX

    fun home(): URI = URI(HOME)

    object Users {
        const val CREATE = "$PREFIX/users"
        const val TOKEN = "$PREFIX/users/token"
        const val LOGOUT = "$PREFIX/logout"
        const val GET_BY_ID = "$PREFIX/users/{id}"
        const val HOME = "$PREFIX/me"

        fun byId(id: Int) = UriTemplate(GET_BY_ID).expand(id)
        fun home(): URI = URI(HOME)
        fun login(): URI = URI(TOKEN)
        fun register(): URI = URI(CREATE)
    }
}