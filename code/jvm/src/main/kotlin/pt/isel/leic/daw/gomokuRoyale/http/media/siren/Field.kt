package pt.isel.leic.daw.gomokuRoyale.http.media.siren

/**
 * Fields represent controls inside of actions.
 *
 * @property name describes the control. Field names MUST be unique within the set of fields for an action
 * @property class describes aspects of the field based on the current representation. Optional
 * @property type the input type of the field. Optional
 * @property value a value assigned to the field. Optional
 * @property title textual annotation of a field. Optional
 */

data class Field(
    val name: String,
    val `class`: List<String>? = null,
    val type: String? = null,
    val value: String? = null,
    val title: String? = null
)
