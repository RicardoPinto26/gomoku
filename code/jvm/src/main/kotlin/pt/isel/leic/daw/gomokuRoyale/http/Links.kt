package pt.isel.leic.daw.gomokuRoyale.http

import pt.isel.leic.daw.gomokuRoyale.http.media.siren.Link
import java.net.URI

object Links {

    fun self(href: URI) = Link(
        rel = listOf(Rels.SELF),
        href = href
    )

    val home = Link(
        rel = listOf(Rels.HOME),
        href = Uris.home()
    )

    val userHome = Link(
        rel = listOf(Rels.USER_HOME),
        href = Uris.userHome()
    )
}
