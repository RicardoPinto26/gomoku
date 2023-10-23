package pt.isel.leic.daw.gomokuRoyale.http.media.siren

data class Field(
    val title: String? = null,
    val name: String,
    val fieldClass: List<String>? = null,
    val type: String? = null,
    val value: String? = null
)