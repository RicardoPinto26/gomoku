package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import java.net.URI

sealed class SubEntity {

    data class EmbeddedLink(
        val rel: List<String>,
        val `class`: List<String>? = null,
        val href: URI,
        val type: String? = null,
        val title: String? = null
    ) : SubEntity()

    data class EmbeddedSubEntity<T>(
        val `class`: List<String>? = null,
        val rel: List<String>,
        val properties: T? = null,
        val entities: List<SubEntity>? = null,
        val links: List<Link>? = null,
        val actions: List<Action>? = null,
        val title: String? = null
    ) : SubEntity()
}
