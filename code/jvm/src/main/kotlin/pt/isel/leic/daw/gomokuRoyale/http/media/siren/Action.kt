package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import java.net.URI

data class Action(
    val name: String,
    val `class`: List<String>? = null,
    val method: Method? = null,
    val href: URI,
    val title: String? = null,
    val type: String? = null,
    val fields: List<Field>? = null
)
