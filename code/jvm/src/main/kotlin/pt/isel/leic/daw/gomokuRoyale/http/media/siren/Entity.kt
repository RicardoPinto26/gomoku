package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import org.springframework.http.MediaType
import java.net.URI

data class Entity(
    val title: String? = null,
    val entityClass: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    val type: MediaType? = null,
)