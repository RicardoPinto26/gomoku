package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import java.net.URI

/**
 * Actions show available behaviors an entity exposes
 *
 * @property name a string that identifies the action to be performed. MUST be unique within the set of actions for an entity
 * @property class describes the nature of an action based on the current representation Optional
 * @property method an enumerated attribute mapping to an HTTP method. Optional
 * @property href the URI of the action
 * @property title descriptive text about the action. Optional
 * @property type the encoding type for the request. Optional
 * @property fields a collection of [Field] elements, expressed as an array of objects in JSON Siren. Optional
 */

data class Action(
    val name: String,
    val `class`: List<String>? = null,
    val method: Method? = null,
    val href: URI,
    val title: String? = null,
    val type: String? = null,
    val fields: List<Field>? = null
)
