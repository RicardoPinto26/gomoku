package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import java.net.URI

/**
 * A sub-entity that is an embedded representation.
 * Embedded sub-entity representations retain all the characteristics of a standard entity,
 * but MUST also contain a rel attribute describing the relationship of the sub-entity to its parent.
 */
sealed class SubEntity {

    /**
     * A sub-entity that is an embedded representation.
     * Embedded sub-entity representations retain all the characteristics of a standard entity,
     * but MUST also contain a rel attribute describing the relationship of the sub-entity to its parent.
     *
     * @property rel defines the relationship of the sub-entity to its parent
     * @property class describes the nature of an entity's content based on the current representation. Optional
     * @property href the URI of the linked sub-entity
     * @property type defines media type of the linked sub-entity. Optional
     * @property title the title of the siren entity. Optional
     */
    data class EmbeddedLink(
        val rel: List<String>,
        val `class`: List<String>? = null,
        val href: URI,
        val type: String? = null,
        val title: String? = null
    ) : SubEntity()

    /**
     * A sub-entity that is an embedded representation.
     * Embedded sub-entity representations retain all the characteristics of a standard entity,
     * but MUST also contain a rel attribute describing the relationship of the sub-entity to its parent.
     *
     * @property class describes the nature of an entity's content based on the current representation. Optional
     * @property rel defines the relationship of the sub-entity to its parent
     * @property properties a set of key-value pairs that describe the state of an entity. Optional
     * @property entities a collection of related sub-entities. Optional
     * @property links a collection of items that describe navigational [Link]s, distinct from entity relationships.Optional
     * @property actions A collection of [Action] objects, represented in JSON Siren as an array such as { "actions": [{ ... }] }.Optional
     * @property title the title of the siren entity. Optional
     */
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
