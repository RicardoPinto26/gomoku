package pt.isel.leic.daw.gomokuRoyale.http.media.siren

/**
 * Siren Entity
 *
 * @see <a href="https://github.com/kevinswiber/siren"">Siren Specification</a>
 *
 * @property title the title of the siren entity. Optional
 * @property sirenClass describes the nature of an entity's content based on the current representation.Optional
 * @property properties a set of key-value pairs that describe the state of an entity.Optional
 * @property entities a collection of related sub-[Entity].Optional
 * @property links a collection of items that describe navigational [Link]s, distinct from entity relationships.Optional
 * @property actions A collection of [Action] objects, represented in JSON Siren as an array such as { "actions": [{ ... }] }.Optional
 */
data class Siren<T>(
    val title: String? = null,
    val sirenClass: List<String>? = null,
    val properties: T? = null,
    val entities: List<Entity>? = null,
    val links: List<Link>? = null,
    val actions: List<Action>? = null
)