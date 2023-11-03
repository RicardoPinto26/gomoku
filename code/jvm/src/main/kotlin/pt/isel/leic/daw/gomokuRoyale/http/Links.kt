package pt.isel.leic.daw.gomokuRoyale.http

import pt.isel.leic.daw.gomokuRoyale.http.media.siren.Link
import java.net.URI

object Links {

    fun self(href: URI) = Link(
        rel = listOf(Rels.SELF),
        href = href
    )
}
