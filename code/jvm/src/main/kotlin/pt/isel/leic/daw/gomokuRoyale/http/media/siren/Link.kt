package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import org.springframework.http.MediaType
import java.net.URI

class Link(
    val title: String? = null,
    val rel: Array<String>,
    val linkClass: List<String>? = null,
    val href: URI,
    val type: MediaType? = null
)