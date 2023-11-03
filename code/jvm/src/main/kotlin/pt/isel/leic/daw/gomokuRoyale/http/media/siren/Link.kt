package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import java.net.URI

data class Link(
    val rel: List<String>,
    val `class`: List<String>? = null,
    val href: URI,
    val type: String? = null,
    val title: String? = null
)
