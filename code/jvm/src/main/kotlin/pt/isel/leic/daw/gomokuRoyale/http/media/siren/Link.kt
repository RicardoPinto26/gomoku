package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import java.net.URI

/**
 * Links represent navigational transitions. In JSON Siren, links are represented as an array inside the entity
 *
 * @property rel defines the relationship of the link to its entity
 * @property class describes aspects of the link based on the current representation. Optional
 * @property href the URI of the linked resource
 * @property title text describing the nature of a link. Optional
 * @property type defines media type of the linked resource,. Optional
 */

data class Link(
    val rel: List<String>,
    val `class`: List<String>? = null,
    val href: URI,
    val type: String? = null,
    val title: String? = null
)
