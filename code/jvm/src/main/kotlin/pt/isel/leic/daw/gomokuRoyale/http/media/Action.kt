package pt.isel.leic.daw.gomokuRoyale.http.media

import java.net.URI

data class Action(
    val name: String,
    val `class`: List<String>? = null,
    val method: String? = null,
    val href: URI,
    val title: String? = null,
    val type: String? = null,
    val fields: List<Field>? = null
)
